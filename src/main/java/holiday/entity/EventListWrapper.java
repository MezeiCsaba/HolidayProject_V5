package holiday.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventListWrapper {
	private List<Event> list = new ArrayList<>();

	public void add(Event event) {
		this.list.add(event);
	}
}
