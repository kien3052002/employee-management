package employee.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import employee.model.Contract;
import employee.model.Department;
import employee.model.Employee;
import employee.service.ContractService;
import employee.service.DepartmentService;
import employee.service.EmployeeService;

@Controller
@RequestMapping("/contracts")
public class ContractController {
	@Autowired
	private ContractService contractService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;

	@GetMapping()
	String viewContractsPage(Model model) {
		List<Contract> listContracts = contractService.getAllContracts();
		List<Department> listDepartments = new ArrayList<>();
		for (Contract c : listContracts) {
			listDepartments.add(departmentService.getDepartmentById(c.getDepartmentId()));
		}
		model.addAttribute("listContracts", listContracts);
		model.addAttribute("listDepartments", listDepartments);
		return "show_contract";
	}

	@GetMapping(value = { "/newContract", "/newContract/{id}" })
	public String showNewContractForm(Model model, @PathVariable(value = "id", required = false) String id) {
		Contract contract = new Contract();
		List<Employee> employees = employeeService.getAllEmployees();
		List<Employee> noContractEmployees = new ArrayList<Employee>();
		if (id == null)
			for (Employee e : employees) {
				if (contractService.getContractById(e.getId()) == null) {
					noContractEmployees.add(e);
				}
			}
		else
			noContractEmployees.add(employeeService.getEmployeeById(Integer.valueOf(id)));
		List<Department> departments = departmentService.getAllDepartments();
		List<Boolean> hasChief=new ArrayList<>();
		for(Department d: departments) {
			if(departmentService.getChief(d.getId())!=null) {
				hasChief.add(true);
			}
			else {
				hasChief.add(false);
			}
		}
		model.addAttribute("hasChief", hasChief);
		model.addAttribute("listDepartments", departments);
		model.addAttribute("contract", contract);
		model.addAttribute("noContractEmployees", noContractEmployees);
		return "new_contract";
	}

	@PostMapping("/saveContract")
	public String saveContract(@ModelAttribute("contract") Contract contract,
			@RequestParam("id_contract") long id_contract, @RequestParam("department") long id,
			@RequestParam("signed") String signed, @RequestParam("start") String start, @RequestParam("end") String end)
			throws ParseException {
		Employee employee = employeeService.getEmployeeById(id_contract);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		contract.setSignedDate(sdf.parse(signed));
		contract.setStartDate(sdf.parse(start));
		contract.setEndDate(sdf.parse(end));
		contract.setId(id_contract);
		contract.setEmployee(employee);
		contract.setDepartmentId(id);
		if (contractService.getContractById(id_contract) != null) {
			contractService.deleteContractById(id_contract);
		}
		contractService.saveContract(contract);
		return "redirect:/contracts";
	}

	@GetMapping("/updateContract/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) throws ParseException {
		Contract contract = contractService.getContractById(id);
		List<Department> departments = departmentService.getAllDepartments();
		Department currDepartment = departmentService.getDepartmentById(contract.getDepartmentId());
		List<Boolean> hasChief=new ArrayList<>();
		for(Department d: departments) {
			if(departmentService.getChief(d.getId())!=null) {
				hasChief.add(true);
			}
			else {
				hasChief.add(false);
			}
		}
		model.addAttribute("contract", contract);
		model.addAttribute("currDepartment", currDepartment);
		model.addAttribute("hasChief", hasChief);
		model.addAttribute("listDepartments", departments);
		return "update_contract";
	}

	@GetMapping("/deleteContract/{id}")
	public String deleteContract(@PathVariable(value = "id") long id) {
		this.contractService.deleteContractById(id);
		return "redirect:/contracts";
	}

	@GetMapping("/detailsContract/{id}")
	public String viewDetails(@PathVariable(value = "id") long id, Model model) {

		Contract contract = contractService.getContractById(id);
		Department department = departmentService.getDepartmentById(contract.getDepartmentId());
		model.addAttribute("contract", contract);
		model.addAttribute("department", department);
		return "details_contract";
	}

}
