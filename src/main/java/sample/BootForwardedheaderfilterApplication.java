package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
public class BootForwardedheaderfilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootForwardedheaderfilterApplication.class, args);
	}

	@Bean
	FilterRegistrationBean forwardedHeaderFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new ForwardedHeaderFilter());
		filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegBean;
	}
}
