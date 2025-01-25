package holiday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sysparams")
@Data
@NoArgsConstructor
@ToString
public class SystemParams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String mailHost;
	private Integer mailPort;
	private String mailUsername;
	private String mailPassword;

	private String transportProtocol;
	private String smtpAuth;
	private String startSslEnable;
	private String mailDebug;

	private String dbPassword;
	private String dbUrl;
	private String dbDriverClassName;
	private String dbUser;

}
