package employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import employee.model.Department;
import employee.model.Employee;
import employee.service.DepartmentService;
import employee.service.EmployeeService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired 
	private EmployeeService employeeService;
	
	@GetMapping()
	String showDepartmentsPage(Model model) {
		List<Department> listDepartments = departmentService.getAllDepartments();
		List<Integer> employeeNumber = new ArrayList<Integer>();
		List<Employee> chiefs = new ArrayList<Employee>();
		for (Department d : listDepartments) {
			int n = departmentService.getEmployees(d.getId()).size();
			employeeNumber.add(n);
			Employee chief = departmentService.getChief(d.getId());
			chiefs.add(chief);
		}
		model.addAttribute("employeeNumber", employeeNumber);
		model.addAttribute("chiefs", chiefs);
		model.addAttribute("listDepartments", listDepartments);
		return "show_department";
	}

	@GetMapping("/newDepartment")
	String showNewDepartmentForm(Model model) {
		Department department = new Department();
		model.addAttribute("department", department);
		return "new_department";
	}
	
	@PostMapping("/saveDepartment")
	public String saveDepartment(@ModelAttribute("department") Department department) {	
		departmentService.saveDepartment(department);
		return "redirect:/";
	}
	
	@GetMapping("/updateDepartment/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		Department department = departmentService.getDepartmentById(id);
		
		model.addAttribute("department", department);
		return "update_department";
	}
	
	@GetMapping("/deleteDepartment/{id}")
	public String deleteDepartment(@PathVariable (value = "id") long id) {
		List<Employee> employees = departmentService.getEmployees(id);
		for(Employee employee : employees) {
			employee.setDepartment(null);
			employeeService.saveEmployee(employee);
		}
		this.departmentService.deleteDepartment(id);
		return "redirect:/departments";
	}
	
	@GetMapping("/detailsDepartment/{id}")
	public String showDetails(@PathVariable ( value = "id") long id, Model model) {
		
		Department department = departmentService.getDepartmentById(id);
		model.addAttribute("department", department);
		return "details_department";
	}
}
