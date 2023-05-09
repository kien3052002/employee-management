package employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import employee.model.User;
import employee.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping()
	String viewUserPage(Model model) {
		List<User> listUsers = userService.getAllUsers();
		model.addAttribute("listUsers", listUsers);
		return "show_user";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute("user") User user, HttpServletRequest request) {	
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	    userService.saveUser(user);
	    if (request.getUserPrincipal().getName().equals(user.getEmail()) && user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER"))) {
	        SecurityContextHolder.clearContext();
	        request.getSession().invalidate();
	        return "redirect:/login";
	    }
        return "redirect:/users";
	}
	@GetMapping("/updateUser/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
		User user = userService.getUserById(id);
	    model.addAttribute("user", user);

		return "update_user";
	}
	@GetMapping("/deleteUser/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id, HttpServletRequest request) {
		User currentUser = userService.getUserByEmail(request.getUserPrincipal().getName());
		this.userService.deleteUserById(id);
	    if (currentUser != null && currentUser.getId() == id) {
	        SecurityContextHolder.clearContext();
	        request.getSession().invalidate();
	        return "redirect:/login";
	    } else {
		return "redirect:/users";
	    }
	}

}
