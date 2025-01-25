package holiday.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER) // cascade = CascadeType.ALL,
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	private Boolean status;
	private Long approverId; // jóváhagyó user Id-ja
	private String activationCode;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonBackReference(value = "userLeaves")
	private List<UserLeaves> userLeaves;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // cascade = CascadeType.ALL,
	@JsonBackReference(value = "events")
	private List<Event> events;

	public User() {
	};

	public User(String name, String email, String password, String uRole, Boolean status) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.addRole(uRole);
		this.status = status;
	}

	public void addRole(String arole) {
		if (this.roles == null || this.roles.isEmpty())
			this.roles = new HashSet<Role>();
		this.roles.add(new Role(arole));
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", roles=" + roles
				+ ", status=" + status + ", approverId=" + approverId + ", activationCode=" + activationCode
				+ ", userLeaves=" + userLeaves + ", events=" + events + "]";
	}

}
