package employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import employee.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
