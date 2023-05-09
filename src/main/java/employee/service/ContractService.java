package employee.service;

import employee.model.Contract;

public interface ContractService {
	List <Contract> getAllContracts();
	Contract getContractById(long id);
	void saveContract(Contract contract);
	void deleteContractById(Long id);
}
