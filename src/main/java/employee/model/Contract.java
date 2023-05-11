package employee.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import employee.service.DepartmentService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "contracts")
public class Contract {

	// Khóa chính dựa vào employee
	@Id
	@Column(name = "employee_id")
	private long id;

	// Ngày kí hợp đông
	@Column(name = "signed_date")
	private Date signedDate;

	// Ngày bắt đầu hiệu lực
	@Column(name = "start_date")
	private Date startDate;

	// Ngày jeets thúc hiệu lực
	@Column(name = "end_date")
	private Date endDate;

	// Tên phòng ban
	@Column(name = "department_id")
	private long departmentId;

	// Chức vụ
	private String position;

	// Quan hệ 1-1 với employee
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	@JoinColumn(name = "employee_id")
	private Employee employee;

	// Lương theo ngày
	@Column(name = "daily_wage")
	private long dailyWage;
	
	public String getDateString(Date date) {
		String s = new SimpleDateFormat("yyyy-MM-dd ss-mm-hh").format(date).split(" ")[0];
		return s;
	}
	
}
