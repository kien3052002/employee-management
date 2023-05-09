package employee.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import employee.model.Department;
import employee.model.Employee;
import employee.service.DepartmentService;
import employee.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@GetMapping
	public String viewEmployeePage(Model model) {
		List<Employee> listEmployees = employeeService.getAllEmployees();
		model.addAttribute("listEmployees", listEmployees);
		return "show_employee";
	}

	@GetMapping("/newEmployee")
	public String showNewEmployeeForm(Model model) {
		Employee employee = new Employee();
		List<Department> departments = departmentService.getAllDepartments();
		Department none = new Department();
		none.setId(0);
		none.setName("(Not Assigned)");
		departments.add(0, none);
		model.addAttribute("listDepartments", departments);
		model.addAttribute("employee", employee);
		return "new_employee";
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee, @RequestParam("department") long id,
			@RequestParam("date") String dob) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
		employee.setDob(date);
		employee.setFullName(employee.getFirstName() + " " + employee.getLastName());
		employee.setDepartment(departmentService.getDepartmentById(id));
		employee.setPosition("Employee");
		employeeService.saveEmployee(employee);
		return "redirect:/employees";
	}

	@GetMapping("/updateEmployee/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) throws ParseException {

		Employee employee = employeeService.getEmployeeById(id);
		Date dob = defaultDate(employee.getDobString());
		List<Department> departments = departmentService.getAllDepartments();
		Department none = new Department();
		none.setId(0);
		none.setName("<Not Assigned>");
		departments.add(0, none);
		model.addAttribute("dob", dob);
		model.addAttribute("listDepartments", departments);
		model.addAttribute("employee", employee);
		return "update_employee";
	}

	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id) {
		Employee employee = employeeService.getEmployeeById(id);
		employee.setDepartment(null);
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/employees";
	}

	@GetMapping("/detailsEmployee/{id}")
	public String viewDetails(@PathVariable(value = "id") long id, Model model) {

		Employee employee = employeeService.getEmployeeById(id);

		model.addAttribute("employee", employee);
		return "details_employee";
	}

	@GetMapping("/attend/{id}")
	public String attend(@PathVariable(value = "id") long id, Model model) throws FileNotFoundException {
		Calendar cal = Calendar.getInstance();
		Employee employee = employeeService.getEmployeeById(id);
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DATE));
		HashMap<String, HashMap<String, String>> mapMonth = employee.getAttendanceMap();
		HashMap<String, String> mapDay = mapMonth.getOrDefault(month, new HashMap<String, String>());
		mapDay.put(day, "1");
		mapMonth.put(month, mapDay);
		employee.setAttendanceMap(mapMonth);
		return "redirect:/employees";
	}

	public Date defaultDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = sdf.parse(date);
		sdf.applyPattern("MM-dd-yyyy");
		date = sdf.format(dob);
		dob = sdf.parse(date);
		return dob;
	}

}