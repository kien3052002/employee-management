package employee.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	private String gender;
	private String position;
	private Date dob;

	private String phone;

	@Column(name = "home_town")
	private String homeTown;

	public String[] getListFromDob() {
		Date date = this.getDob();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(date).split(" ")[0];
		String[] DoB = strDate.split("-");
		return DoB;
	}

	public void setDobFromString(String day, String month, String year) throws ParseException {
		if (day.length() < 2)
			day = "0" + day;
		if (month.length() < 2)
			day = "0" + month;
		String dob = year + "/" + month + "/" + day;
		Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dob);
		this.setDob(date);
	}

	public String getStringFromDob() {
		Date date = this.getDob();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(date).split(" ")[0];
		return strDate;
	}
}