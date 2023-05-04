package employee.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import employee.dto.UserRegistrationDto;
import employee.model.User;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
