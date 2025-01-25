package holiday.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;
	@ManyToOne
	private User user;
	private Byte approved; // -1: denied, 0: pending approval, 1: approved
	private Byte duration; // 1: egész nap, 2: délelőtt, 3 : délután

	public Event(LocalDate startDate, User user, Byte approved, Byte duration) {
		this.startDate = startDate;
		this.user = user;
		this.approved = approved;
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", startDate=" + startDate + ", user=" + user.getName() + ", approved=" + approved
				+ ", duration=" + duration + "]";
	}
}
