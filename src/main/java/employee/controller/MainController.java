package employee.controller;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import employee.model.Employee;
import employee.service.EmployeeService;

@Controller
public class MainController {

	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/login")
	public String login() {
		List<Employee> employees = employeeService.getAllEmployees();
		for (Employee e:employees) {
			if(e.contracted())
				try {
					e.attendanceMapUpdate();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
		}
		return "login";
	}

	@GetMapping("/")
	public String home(Model model) {
		List<Employee> employees = employeeService.getAllEmployees();
		for (Employee e:employees) {
			if(e.contracted())
				try {
					e.attendanceMapUpdate();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
		}
		return "index";
	}
	
	

}
