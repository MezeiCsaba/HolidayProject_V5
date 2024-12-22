package holiday.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import holiday.entity.UserLeaves;

public interface LeaveRepository  extends JpaRepository<UserLeaves, Long> {

	Set<UserLeaves> findAllByUserId(Long userId);
	Set<UserLeaves> findByUserIdAndLeaveYearBetween(Long userId, int startYear, int endYear);
	UserLeaves findByUserIdAndLeaveYear(Long id, Integer actYear);




}
