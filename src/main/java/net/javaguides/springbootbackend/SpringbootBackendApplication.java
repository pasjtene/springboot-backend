package net.javaguides.springbootbackend;

import net.javaguides.springbootbackend.model.User;
import net.javaguides.springbootbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringbootBackendApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBackendApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/deleteusers").allowedOrigins("http://localhost:3000");
			}
		};
	}


	@Override
	public void run(String... args) throws Exception {
		this.userRepository.save(new User("Pascal", "jt", "jt@j","jt.pas","jt@j","USER"));
		this.userRepository.save(new User("Brian", "tj", "jt@jon","jt.pas","jt@jon","ADMIN"));
		this.userRepository.save(new User("Dylan", "wj", "wjt@jon", "jt.pas","wjt@jon","USER"));

	}




}
