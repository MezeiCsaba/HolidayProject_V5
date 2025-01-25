package holiday.entity;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApprovePojo {
	
	private long eventId;
	private long userId;
	private String userName;
	private String userEmail;
	private LocalDate startDate;
	private Byte approved; // -1: denied, 0: pending approval, 1: approved
	private Byte duration;

	public ApprovePojo(String userName, LocalDate startDate, Byte approved) {
		this.userName = userName;
		this.startDate = startDate;
		this.approved = approved;
	}
}
