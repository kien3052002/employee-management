package employee.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email")
	private String email;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Contract contract;

	private String gender;
	private String position;

	private Date dob;

	private String phone;

	@Column(name = "home_town")
	private String homeTown;

	public String getDobString() {
		String s = new SimpleDateFormat("yyyy-MM-dd ss-mm-hh").format(this.getDob()).split(" ")[0];
		return s;
	}

	@Column(name = "monthly_attendance")
	private String monthlyAttend;

	@Column(name = "last_month_attendance")
	private String lastMonthAttend;

	@Column(name = "monthly_salary")
	private String monthlySalary;

	@Column(name = "last_month_salary")
	private String lastMonthSalary;

	public HashMap<String, Integer> getAttendanceMap(String attendance, String salary) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String[] attendanceList = attendance.split(" ");
		String[] salaryList = salary.split(" ");
		for (int i = 0; i < attendanceList.length; i++) {
			map.put(attendanceList[i], Integer.valueOf(salaryList[i]));
		}
		return map;
	}

}