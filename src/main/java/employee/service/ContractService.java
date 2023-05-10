package employee.service;

import java.util.List;

import employee.model.Contract;
import employee.model.Employee;

public interface ContractService {
	List <Contract> getAllContracts();
	Contract getContractById(long id);
	void saveContract(Contract contract);
	void deleteContractById(Long id);
	Employee getEmployee(long id);
}
