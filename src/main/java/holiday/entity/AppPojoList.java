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
public class AppPojoList {
	
	private List<ApprovePojo> list = new ArrayList<>();

	public void add(ApprovePojo aPojo) {
		this.list.add(aPojo);
	}
}
