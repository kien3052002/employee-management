package employee.controller;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import employee.model.Dates;
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
		model.addAttribute("employee", employee);
		return "new_employee";
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee, @RequestParam("date") String dob)
			throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
		employee.setDob(date);
		employee.setFullName(employee.getFirstName() + " " + employee.getLastName());
		employee.setPosition("Employee");
		employeeService.saveEmployee(employee);
		return "redirect:/employees";
	}

	@GetMapping("/updateEmployee/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) throws ParseException {

		Employee employee = employeeService.getEmployeeById(id);
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

	@GetMapping(value = { "/attend/{id}/now", "/attend/{id}/{day}_{month}" })
	public String attend(@PathVariable(value = "id") long id, Model model,
			@PathVariable(value = "day", required = false) String d,
			@PathVariable(value = "month", required = false) String m) throws FileNotFoundException {
		String month;
		String day;
		System.out.println(m + d);
		if (d == null && m == null) {
			Calendar cal = Calendar.getInstance();
			month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
			day = String.format("%02d", cal.get(Calendar.DATE));
		} else {
			month = m;
			day = d;
		}
		Employee employee = employeeService.getEmployeeById(id);
		HashMap<String, HashMap<String, String>> mapMonth = employee.getAttendanceMap();
		HashMap<String, String> mapDay = mapMonth.getOrDefault(month, new HashMap<String, String>());
		mapDay.put(day, "1");
		mapMonth.put(month, mapDay);
		employee.setAttendanceMap(mapMonth);
		return "redirect:/employees";
	}

	@GetMapping("/calendar/{id}/{month}")
	public String showCalendar(@PathVariable(value = "id") long id,
			@PathVariable(value = "month", required = false) String m, Model model) {
		int month;
		if (m.equals("now"))
			month = Dates.currMonth() - 1;
		else
			month = Integer.valueOf(m) - 1;
		if (month < 0 || month > 12)
			return null;

		Employee employee = employeeService.getEmployeeById(id);
		List<String> calDays = Dates.daysOfMonth(month);
		HashMap<String, HashMap<String, String>> attendanceMap = employee.getAttendanceMap();
		int n = calDays.size();
		String[] days = new String[n];
		String[] months = new String[n];
		int[] attendance = new int[n];

		for (int i = 0; i < n; i++) {
			days[i] = calDays.get(i).split("-")[2];
			months[i] = calDays.get(i).split("-")[1];
			if (attendanceMap.get(months[i]) == null || attendanceMap.get(months[i]).get(days[i]) == null)
				attendance[i] = -1;
			else
				attendance[i] = Integer.valueOf(attendanceMap.get(months[i]).get(days[i]));
		}
		model.addAttribute("currMonth", String.format("%02d", Dates.currMonth()));
		model.addAttribute("thisDay", Dates.currDate());
		model.addAttribute("maxDays", Dates.dateIndexLimit());
		model.addAttribute("weekNum", (n + 6) / 7);
		model.addAttribute("attendance", attendance);
		model.addAttribute("days", days);
		model.addAttribute("months", months);
		model.addAttribute("employee", employee);
		model.addAttribute("thisMonth", month + 1);
		return "show_calendar";
	}

}