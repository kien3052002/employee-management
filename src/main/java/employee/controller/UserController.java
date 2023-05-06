package employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import employee.model.User;
import employee.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping()
	String viewUserPage(Model model) {
		List<User> listUsers = userService.getAllUsers();
		model.addAttribute("listUsers", listUsers);
		return "show_user";
	}

	@GetMapping("/updateUser/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		User user = userService.getUserById(id);

		model.addAttribute("employee", user);
		return "update_user";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id) {

		this.userService.deleteUserById(id);
		return "redirect:/users";
	}

}
