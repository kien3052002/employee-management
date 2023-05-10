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
		for(Contract c : listContracts) {
			listDepartments.add(departmentService.getDepartmentById(c.getDepartmentId()));
		}
		model.addAttribute("listContracts", listContracts);
		model.addAttribute("listDepartments", listDepartments);
		return "show_contract";
	}

	@GetMapping("/newContract")
	public String showNewContractForm(Model model) {
		Contract contract = new Contract();
		List<Employee> employees = employeeService.getAllEmployees();
		List<Employee> noContractEmployees = new ArrayList<Employee>();
		for (Employee e : employees) {
			if (contractService.getContractById(e.getId()) == null) {
				noContractEmployees.add(e);
			}
		}
		List<Department> departments = departmentService.getAllDepartments();
		model.addAttribute("listDepartments", departments);
		model.addAttribute("contract", contract);
		model.addAttribute("noContractEmployees", noContractEmployees);
		return "new_contract";
	}

	@PostMapping("/saveContract")
	public String saveEmployee(@ModelAttribute("contract") Contract contract,
			@RequestParam("id_contract") long id_contract, @RequestParam("department") long id,
			@RequestParam("signed") String signed, @RequestParam("start") String start, @RequestParam("end") String end)
			throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		contract.setSignedDate(sdf.parse(signed));
		contract.setStartDate(sdf.parse(start));
		contract.setEndDate(sdf.parse(end));
		contract.setId(id_contract);
		contract.setEmployee(employeeService.getEmployeeById(id_contract));
		contract.setPosition(employeeService.getEmployeeById(id_contract).getPosition());
		contract.setName(employeeService.getEmployeeById(id_contract).getFirstName() + " "
				+ employeeService.getEmployeeById(id_contract).getLastName());
		if (departmentService.getDepartmentById(id) == null)
			contract.setDepartmentId(0);
		else
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
		Date currDate = Calendar.getInstance().getTime();
		Date startDate = contract.getStartDate();
		Boolean canUpdate = true;
		if (startDate.compareTo(currDate) <= 0) {
			canUpdate = false;
		}
		List<Department> departments = departmentService.getAllDepartments();
		Department currDepartment = departmentService.getDepartmentById(contract.getDepartmentId());
		model.addAttribute("canUpdate", canUpdate);
		model.addAttribute("contract", contract);
		model.addAttribute("currDepartment", currDepartment);
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
