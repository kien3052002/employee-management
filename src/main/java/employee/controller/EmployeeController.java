package employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import employee.model.Department;
import employee.model.Employee;
import employee.service.DepartmentService;
import employee.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@GetMapping
	public String viewEmployeePage(Model model) {
		List<Employee> listEmployees = employeeService.getAllEmployees();
		model.addAttribute("listEmployees", listEmployees);
		return "show_employee";
	}

	@GetMapping("/newEmployee")
	public String showNewEmployeeForm(Model model) {
		Employee employee = new Employee();
		List<Department> departments = departmentService.getAllDepartments();
		model.addAttribute("listDepartments", departments);
		model.addAttribute("employee", employee);
		return "new_employee";
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee, @RequestParam("department") long id) {
		employee.setFullName(employee.getFirstName() + " " + employee.getLastName());
		employee.setDepartment(departmentService.getDepartmentById(id));
		employeeService.saveEmployee(employee);
		return "redirect:/employees";
	}

	@GetMapping("/updateEmployee/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		Employee employee = employeeService.getEmployeeById(id);
		List<Department> departments = departmentService.getAllDepartments();
		Department currDepartment = employee.getDepartment();
		model.addAttribute("currDepartment", currDepartment);
		model.addAttribute("listDepartments", departments);
		model.addAttribute("employee", employee);
		return "update_employee";
	}

	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id) {
		Employee employee = employeeService.getEmployeeById(id);
		employee.setDepartment(null);
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/employees";
	}

	@GetMapping("/detailsEmployee/{id}")
	public String viewDetails(@PathVariable(value = "id") long id, Model model) {

		Employee employee = employeeService.getEmployeeById(id);

		model.addAttribute("employee", employee);
		return "details_employee";
	}

}