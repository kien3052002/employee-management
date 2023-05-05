package employee.service;

import java.util.List;

import employee.model.Department;
import employee.model.Employee;

public interface DepartmentService {
	List<Department> getAllDepartments();
	void saveDepartment(Department department);
	Department getDepartmentById(long id);
	List<Employee> getEmployees(long id);
	Employee getChief(long id);
	void deleteDepartment(long id);
}
