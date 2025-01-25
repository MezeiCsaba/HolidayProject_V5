package holiday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "userLeaves")
@Getter
@Setter
@ToString
public class UserLeaves {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User user;
	@Column(unique = true, nullable = false)
	private Integer leaveYear;
	private Integer baseLeave;
	private Integer parentalLeave;
	private Integer carriedLeave;
	private Integer otherLeave;

	public UserLeaves(User user, Integer actYear, Integer baseLeave, Integer parentalLeave, Integer carriedLeave,
			Integer otherLeave) {
		this.user = user;
		this.leaveYear = actYear;
		this.baseLeave = baseLeave;
		this.parentalLeave = parentalLeave;
		this.carriedLeave = carriedLeave;
		this.otherLeave = otherLeave;
	}

	public UserLeaves() {
	}

	public Integer getSumLeaveFrame() {
		return (baseLeave == null && parentalLeave == null && carriedLeave == null && otherLeave == null) ? 0
				: (baseLeave + parentalLeave + carriedLeave + otherLeave);
	}
}
