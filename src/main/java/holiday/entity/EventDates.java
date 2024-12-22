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

@Entity
public class EventDates implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@DateTimeFormat (iso = DateTimeFormat.ISO.DATE)
	@Column(unique = true, nullable = false)
	private LocalDate date;
	private String note;
	private Boolean isWorkDay;
	
	
	
	public EventDates() {}

	public EventDates(LocalDate date, String note, Boolean isWorkDay) {
		this.date = date;
		this.note = note;
		this.isWorkDay = isWorkDay;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate Date) {
		this.date = Date;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Boolean getIsWorkDay() {
		return isWorkDay;
	}


	public void setIsWorkDay(Boolean isWorkDay) {
		this.isWorkDay = isWorkDay;
	}



	@Override
	public String toString() {
		return "EventDates [id=" + id + ", Date=" + date + ", note=" + note + ", isWorkDay=" + isWorkDay + "]";
	}
	
	

}
