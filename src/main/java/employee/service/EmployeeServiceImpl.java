package employee.service;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import employee.model.Contract;
import employee.model.Department;
import employee.model.Employee;
import employee.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public void saveEmployee(Employee employee) {

		this.employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		Employee employee = null;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			return null;
		}
		return employee;
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
	}

	@Override
	public List<Employee> getEmployeesByDepartment(long id) {
		return employeeRepository.findByDepartmentId(id);
	}

	@Override
	public void attend(long id, String m, String d) throws FileNotFoundException {
		Calendar cal = Calendar.getInstance();
		int wd = cal.get(Calendar.DAY_OF_WEEK);
		if (wd != 1 || wd != 7) {

			String month;
			String day;
			System.out.println(m + d);
			if (d == null && m == null) {

				month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
				day = String.format("%02d", cal.get(Calendar.DATE));
			} else {
				month = m;
				day = d;
			}
			String value = "1";
			int hr = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			if (hr > 15) {
				if (min > 15)
					value = "0";
			} else if (hr > 10) {
				if (min > 15)
					value = "0.5";
			}
			Employee employee = this.getEmployeeById(id);
			HashMap<String, HashMap<String, String>> mapMonth = employee.getAttendanceMap();
			HashMap<String, String> mapDay = mapMonth.getOrDefault(month, new HashMap<String, String>());
			employee.attendanceMapUpdate();
			mapDay.put(day, value);
			mapMonth.put(month, mapDay);
			employee.setAttendanceMap(mapMonth);
		}
	}
}