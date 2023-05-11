package employee.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.data.domain.Page;

import employee.model.Employee;

public interface EmployeeService {
	List<Employee> getAllEmployees();
	void saveEmployee(Employee employee);
	Employee getEmployeeById(long id);
	void deleteEmployeeById(long id);
	List<Employee> getEmployeesByDepartment(long id);
	void attend(long id, String day, String month) throws FileNotFoundException;
}
