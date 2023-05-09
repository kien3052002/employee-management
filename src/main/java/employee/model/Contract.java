package employee.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "contracts")
public class Contract {

	// Khóa chính dựa vào employee
	@Id
	@Column(name = "employee_id")
	private long id;

	@Column(name = "name")
	private String name;

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
	@Column(name = "department_name")
	private String departmentName;

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

	
}
