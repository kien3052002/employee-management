package employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import employee.dto.UserRegistrationDto;
import employee.model.Employee;
import employee.model.User;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	List<User> getAllUsers();
	User getUserById(long id);
	void deleteUserById(long id);
	void saveUser(User user);
	User getUserByEmail(String email);	
}
