package ops.school;


import com.alibaba.druid.pool.DruidDataSource;
import ops.school.api.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;




//@EnableFeignClients
//@EnableEurekaClient
@EnableScheduling
@SpringBootApplication
@MapperScan("ops.school.api.dao")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource druidDataSource() {
		return new DruidDataSource();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public SpringUtil springUtil() {
		return new SpringUtil();
	}

}
