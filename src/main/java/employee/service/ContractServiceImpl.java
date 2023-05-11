package employee.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import employee.model.Contract;
import employee.model.Employee;
import employee.repository.ContractRepository;


@Service
public class ContractServiceImpl implements ContractService {
	
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Override
	public void saveContract(Contract contract) {
		
		Employee employee = contract.getEmployee();
		HashMap<String, HashMap<String, String>> map = employee.getAttendanceMap();
		employee.setDepartment(departmentService.getDepartmentById(contract.getDepartmentId()));
		employee.setPosition(contract.getPosition());
		try {
			employee.setAttendanceMap(map);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.employeeService.saveEmployee(employee);
		this.contractRepository.save(contract);
	}

	@Override
	public void deleteContractById(Long id) {
		Contract contract = this.getContractById(id);
		contract.setEmployee(null);
		Employee employee = this.getEmployee(id);
		employee.setContract(null);
		employee.setDepartment(null);
		employee.setPosition("Employee");
		employeeService.saveEmployee(employee);
		employee.deleteAttendanceMap();
		this.contractRepository.deleteById(id);
	}

	@Override
	public List<Contract> getAllContracts() {
		return contractRepository.findAll();
	}

	@Override
	public Contract getContractById(long id) {
		Optional<Contract> optional = contractRepository.findById(id);
		Contract contract = null;
		if (optional.isPresent()) {
			contract = optional.get();
		} else {
			return null;
		}
		return contract;
	}
	@Override
	public Employee getEmployee(long id) {
		return employeeService.getEmployeeById(id);
	}
}
