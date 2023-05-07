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
import org.springframework.web.bind.annotation.RequestParam;

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
	public String saveDepartment(@ModelAttribute("department") Department department,
			@RequestParam(name = "toRemove", required = false) String toRemove,
			@RequestParam(name = "toAdd", required = false) String toAdd) {
		List<Employee> removeList = toEmpList(toRemove);
		List<Employee> addList = toEmpList(toAdd);
		for (Employee e : removeList) {
			e.setDepartment(null);
			e.setPosition("Employee");
		}
		for (Employee e : addList) {
			e.setDepartment(department);
			employeeService.saveEmployee(e);
		}
		departmentService.saveDepartment(department);
		return "redirect:/";
	}

	@GetMapping("/updateDepartment/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		Department department = departmentService.getDepartmentById(id);
		List<Employee> inDepartment = departmentService.getEmployees(id);
		List<Employee> available = new ArrayList<Employee>();
		List<Employee> listEmployee = employeeService.getAllEmployees();
		for (Employee e : listEmployee) {
			if (e.getDepartment() == null)
				available.add(e);
		}
		model.addAttribute("inDepartment", inDepartment);
		model.addAttribute("available", available);
		model.addAttribute("department", department);
		return "update_department";
	}

	@GetMapping("/deleteDepartment/{id}")
	public String deleteDepartment(@PathVariable(value = "id") long id) {
		List<Employee> employees = departmentService.getEmployees(id);
		for (Employee employee : employees) {
			employee.setDepartment(null);
			employeeService.saveEmployee(employee);
		}
		this.departmentService.deleteDepartment(id);
		return "redirect:/departments";
	}

	@GetMapping("/detailsDepartment/{id}")
	public String showDetails(@PathVariable(value = "id") long id, Model model) {
		List<Employee> employees = employeeService.getEmployeesByDepartment(id);
		Department department = departmentService.getDepartmentById(id);
		Employee chief = departmentService.getChief(id);
		int employeeNumber = employees.size();
		Boolean hasChief;
		if (chief == null)
			hasChief = false;
		else
			hasChief = true;
		model.addAttribute("hasChief", hasChief);
		model.addAttribute("department", department);
		model.addAttribute("listEmployees", employees);
		model.addAttribute("chief", chief);
		model.addAttribute("number", employeeNumber);
		return "details_department";
	}

	private List<Employee> toEmpList(String s) {
		if(s == null) return new ArrayList<Employee>();
		List<Employee> employees =  new ArrayList<Employee>();
		String[] list = s.split(",");
		for(int i=0;i<list.length;i++) {
			Employee e = employeeService.getEmployeeById(Long.valueOf(list[i]));
			employees.add(e);
		}
		return employees;
	}
}
