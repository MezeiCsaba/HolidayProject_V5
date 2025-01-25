package holiday.services;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import holiday.entity.Event;
import holiday.entity.EventDates;
import holiday.entity.Role;
import holiday.entity.User;
import holiday.entity.UserLeaves;
import holiday.repository.RoleRepository;
import holiday.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Value("${webpage.home.path}")
	private String activationLink;
	private UserRepository userRepo;
	private RoleRepository roleRepo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private ExecutorService emailExecutor = Executors.newFixedThreadPool(10);
	private JavaMailSender javaMailSender;
	private PasswordEncoder passwordEncoder;
	private LeaveService leaveService;
	private EventService eventService;
	private EventsDatesService eventsDatesService;

	@Autowired
	public void setLeaveService(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	@Autowired
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	@Autowired
	public void setEventsDatesService(EventsDatesService eventsDatesService) {
		this.eventsDatesService = eventsDatesService;
	}

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setRoleRepo(RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}

	@Autowired
	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public List<User> getAllUser() {
		return userRepo.findAllByOrderByName();

	}

	public User getUser(String user) {
		// userRepo.findAllByUserNameIgnorCaseOrderByUserNameDesc(user)

		return userRepo.findFirstByNameIgnoreCase(user);
	}

	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User findById(Long id) {
		return userRepo.findFirstById(id);
	}

	public User findByName(String name) {
		return userRepo.findFirstByNameIgnoreCase(name);
	}

	public Page<User> findByOrderByName(Pageable pageable) {
		return userRepo.findByOrderByName(pageable);
	}

	public Page<User> findByOrderByRole(String name, Pageable pageable) {
		return userRepo.findByNameContainingIgnoreCaseOrderByRoles(name, pageable);
	}

	public Page<User> findByOrderByEmail(String name, Pageable pageable) {
		return userRepo.findByNameContainingIgnoreCaseOrderByEmail(name, pageable);
	}

	public Page<User> findByOrderByStatus(String name, Pageable pageable) {
		return userRepo.findByNameContainingIgnoreCaseOrderByStatus(name, pageable);
	}

	public List<User> findByStatus(Boolean status) {
		return userRepo.findByStatus(status);
	}

	public Page<User> findByNameContaining(String name, Pageable pageable) {
		return userRepo.findByNameContainingIgnoreCaseOrderByName(name, pageable);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

	public void checkRoles(User user) {
		for (Role role : user.getRoles()) {
			Role userRole = roleRepo.findByRoleName(role.getRoleName());
			if (userRole != null) {
				user.getRoles().add(userRole);
			} else {
				user.addRole(role.getRoleName());
			}
		}
	}

	public void registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (isNewUser(user)) {
			sendActivationEmail(user);
			user.setStatus(false);
		} else {
			user.setActivationCode("");
			user.setStatus(true);
		}
		checkRoles(user);
		userRepo.save(user);
	}

	private boolean isNewUser(User user) {
		return user.getId() == null || user.getId() < 0;
	}

	private void sendActivationEmail(User user) {
		user.setActivationCode(generateActivationCode());
		String newActivationLink = activationLink + "activation/" + user.getActivationCode();
		emailExecutor.execute(new EmailService(user,
				"Regisztrációs értesítés a Holiday szabadságnyilvántartó rendszerhez",
				" Sikeresen regisztráltak a(z) " + user.getEmail()
						+ " e-mail címmel a Holiday szabadságnyilvántartó rendszerbe. \n\n A regisztráció aktiválásához látogass el a következő linkre: "
						+ newActivationLink,
				javaMailSender));
		user.setStatus(false);
		log.debug("Email kiküldve: " + user.getEmail());
	}

	private String generateActivationCode() {
		Random random = new Random();
		char[] code = new char[16];
		for (int i = 0; i < code.length; i++)
			code[i] = (char) ('a' + random.nextInt(26));
		return new String(code);
	}

	public void updateUserAsAdmin(User updateUser, Boolean chgEmail) {
		String updatePassword = getUpdatedPassword(updateUser);
		updateUser.setPassword(updatePassword);
		checkRoles(updateUser);
		if (chgEmail && !isEmailSent(updateUser)) {
			sendActivationEmail(updateUser);
		}
		userRepo.save(updateUser);
	}

	private String getUpdatedPassword(User updateUser) {
		String dbPassword = userRepo.findFirstById(updateUser.getId()).getPassword();
		CharSequence thisPassword = updateUser.getPassword();
		if (isPasswordUnchanged(thisPassword, dbPassword)) {
			return dbPassword;
		} else {
			sendActivationEmail(updateUser);
			return passwordEncoder.encode(thisPassword);
		}
	}

	private boolean isPasswordUnchanged(CharSequence thisPassword, String dbPassword) {
		return thisPassword.equals(dbPassword) || passwordEncoder.matches(thisPassword, dbPassword) || thisPassword == null;
	}

	private boolean isEmailSent(User updateUser) {
		return updateUser.getActivationCode() != null && !updateUser.getActivationCode().isEmpty();
	}

	public Long isCodeValid(String code) {
		User repUser = userRepo.findFirstByActivationCode(code);
		return repUser == null ? -1L : repUser.getId();
	}

	public Model setPageAttributums(User actUser, Model model) {
		Long actUserId = actUser.getId();
		Integer thisYear = Year.now().getValue();
		UserLeaves userLeaves = getUserLeaves(actUser, thisYear);
		Double[] userSumLeaves = getUserSumLeaves(actUserId, thisYear, userLeaves);
		String approverName = getApproverName(actUser);
		List<Event> eventList = getUserEvents(actUserId);
		List<EventDates> exEventList = eventsDatesService.getAllEvents(thisYear);

		model.addAttribute("approverName", approverName);
		model.addAttribute("user", actUser);
		model.addAttribute("userSumLeaves", userSumLeaves);
		model.addAttribute("userLeaves", userLeaves);
		model.addAttribute("eventList", eventList);
		model.addAttribute("exEventList", exEventList);

		return model;
	}

	private UserLeaves getUserLeaves(User actUser, Integer thisYear) {
		UserLeaves userLeaves = leaveService.getUserLeavesByYear(thisYear, actUser);
		return userLeaves == null ? new UserLeaves() : userLeaves;
	}

	private Double[] getUserSumLeaves(Long actUserId, Integer thisYear, UserLeaves userLeaves) {
		Double[] userSumLeaves = new Double[2];
		Arrays.fill(userSumLeaves, 0D);
		userSumLeaves[0] = eventService.getUserSumLeave(actUserId, thisYear);
		userSumLeaves[1] = Double.valueOf(userLeaves.getSumLeaveFrame());
		return userSumLeaves;
	}

	private String getApproverName(User actUser) {
		if (actUser.getApproverId() == null) {
			return " nincs";
		}
		User approver = findById(actUser.getApproverId());
		return approver.getName() + " (" + approver.getEmail() + ")";
	}

	private List<Event> getUserEvents(Long actUserId) {
		List<Event> eventList = eventService.getUserEvents(actUserId);
		eventList.forEach(e -> e.setUser(null));
		return eventList;
	}

	public void sendTestEmail(User user) {
		emailExecutor.execute(new EmailService(user, "TEST email a Holiday szabadságnyilvántartó rendszertől",
				" Sikeresen TEST  " + user.getEmail() + " e-mail címmel a Holiday szabadságnyilvántartó rendszerből.",
				javaMailSender));
		log.debug("Email kiküldve: " + user.getEmail());
	}

}
