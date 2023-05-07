package employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import employee.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long>{

}
