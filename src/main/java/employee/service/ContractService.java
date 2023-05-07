package employee.service;

import employee.model.Contract;

public interface ContractService {
	void saveContract(Contract contract);
	void deleteContractById(Long id);
}
