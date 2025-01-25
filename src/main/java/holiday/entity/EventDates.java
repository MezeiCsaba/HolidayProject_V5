package holiday.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class EventDates implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(unique = true, nullable = false)
	private LocalDate date;
	private String note;
	private Boolean isWorkDay;

	public EventDates() {
	}

	public EventDates(LocalDate date, String note, Boolean isWorkDay) {
		this.date = date;
		this.note = note;
		this.isWorkDay = isWorkDay;
	}

}
