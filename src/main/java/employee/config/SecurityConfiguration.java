package employee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import employee.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UserService userService;

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/registration**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/img/**").permitAll().requestMatchers("/employees**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/employees/detailsEmployee/**").hasAnyRole("USER", "ADMIN").requestMatchers("/employees/attend/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/employees/calendar/**").hasAnyRole("USER", "ADMIN").requestMatchers("/employees/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers("/departments**").hasAnyRole("USER", "ADMIN").requestMatchers("/departments/detailsDepartment/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/departments/**").hasAuthority("ROLE_ADMIN").requestMatchers("/users**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/users/updateUser/**").hasAnyRole("USER", "ADMIN").requestMatchers("/users/deleteUser/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers("/contracts**").hasAnyRole("USER", "ADMIN").requestMatchers("/contracts/detailsContract/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
}
