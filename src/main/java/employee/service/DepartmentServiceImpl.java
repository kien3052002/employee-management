package employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import employee.model.Department;
import employee.model.Employee;
import employee.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private EmployeeService employeeService;
	
	@Override
	public List<Department> getAllDepartments(){
		return departmentRepository.findAll();
	}
	
	@Override
	public void saveDepartment(Department department) {
		this.departmentRepository.save(department);
	}
	
	@Override
	public List<Employee> getEmployees(long id){
		return employeeService.getEmployeesByDepartment(id);
	}
	
	@Override
	public Employee getChief(long id) {
		for (Employee e : this.getEmployees(id)) {
			if(e.getPosition() == "CHIEF") return e;
		}
		return null;
	}
	
	@Override
	public void deleteDepartment(long id) {
		this.departmentRepository.deleteById(id);
	}

	@Override
	public Department getDepartmentById(long id) {
		Optional<Department> optional = departmentRepository.findById(id);
		Department department = null;
		if (optional.isPresent()) {
			department = optional.get();
		} else {
			throw new RuntimeException(" Department not found for id :: " + id);
		}
		return department;
	}
	
}
