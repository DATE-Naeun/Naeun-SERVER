package date_naeun.naeunserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class NaeunServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaeunServerApplication.class, args);
	}

}
