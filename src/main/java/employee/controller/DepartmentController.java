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
import employee.model.Contract;
import employee.service.ContractService;
import employee.service.DepartmentService;
import employee.service.EmployeeService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ContractService contractService;
	
	@GetMapping()
	String showDepartmentsPage(Model model) {
		List<Department> listDepartments = departmentService.getAllDepartments();
		List<Integer> employeeNumber = new ArrayList<Integer>();
		List<Employee> chiefs = new ArrayList<Employee>();
		for (Department d : listDepartments) {

			employeeNumber.add(departmentService.getEmployees(d.getId()).size());
			chiefs.add(departmentService.getChief(d.getId()));
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
		return "redirect:/departments";
	}

	@GetMapping("/updateDepartment/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

		Department department = departmentService.getDepartmentById(id);
		model.addAttribute("department", department);
		return "update_department";
	}

	@GetMapping("/deleteDepartment/{id}")
	public String deleteDepartment(@PathVariable(value = "id") long id) {
		List<Contract> contracts = contractService.getAllContracts();
		for (Contract c : contracts) {
			if(c.getDepartmentId() == id)
				contractService.deleteContractById(c.getId());
		}
		this.departmentService.deleteDepartment(id);
		return "redirect:/departments";
	}

	@GetMapping("/detailsDepartment/{id}")
	public String showDetails(@PathVariable(value = "id") long id, Model model) {
		List<Employee> employees = employeeService.getEmployeesByDepartment(id);
		Department department = departmentService.getDepartmentById(id);
		Employee chief = departmentService.getChief(id);
		int employeeNumber;
		Boolean hasChief;
		if (chief == null)
			hasChief = false;
		else
			hasChief = true;
		if (employees == null)
			employeeNumber = 0;
		else
			employeeNumber = employees.size();
		model.addAttribute("hasChief", hasChief);
		model.addAttribute("department", department);
		model.addAttribute("listEmployees", employees);
		model.addAttribute("chief", chief);
		model.addAttribute("number", employeeNumber);
		return "details_department";
	}

	private List<Employee> toEmpList(String s) {
		if (s == null)
			return new ArrayList<Employee>();
		List<Employee> employees = new ArrayList<Employee>();
		String[] list = s.split(",");
		for (int i = 0; i < list.length; i++) {
			Employee e = employeeService.getEmployeeById(Long.valueOf(list[i]));
			employees.add(e);
		}
		return employees;
	}
}
