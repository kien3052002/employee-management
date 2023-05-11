package employee.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

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
import jakarta.persistence.Transient;
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


	public HashMap<String, HashMap<String, String>> getAttendanceMap() {
		try {
			InputStream fis = new FileInputStream("src/main/resources/attendance/attendance-" + String.format("%02d", this.getId()) + ".json");
			JsonReader jsonReader = Json.createReader(fis);
			JsonObject jsonObject = jsonReader.readObject();

			jsonReader.close();
			fis.close();

			HashMap<String, HashMap<String, String>> monthMap = new HashMap<String, HashMap<String, String>>();
			for (int i = 1; i < 13; i++) {
				String month = String.format("%02d", i);
				JsonObject obj = jsonObject.getJsonObject(month);

				if (obj != null) {
					HashMap<String, String> dayMap = new HashMap<>();
					for (int k = 1; k < 32; k++) {
						String day = String.format("%02d", k);

						if (obj.get(day) != null) {
							dayMap.put(day, obj.getString(day));
						}

					}
					monthMap.put(month, dayMap);
				}
			}
			return monthMap;
		} catch (Exception e) {
			return newAttendanceMap();
		}
	}

	public void setAttendanceMap(HashMap<String, HashMap<String, String>> map) throws FileNotFoundException {
		JsonObjectBuilder monthBuilder = Json.createObjectBuilder();

		for (int i = 1; i < 13; i++) {
			String month = String.format("%02d", i);
			if (map.get(month) != null) {
				JsonObjectBuilder dayBuilder = Json.createObjectBuilder();
				for (int k = 1; k < 32; k++) {
					String day = String.format("%02d", k);
					if (map.get(month).get(day) != null) {
						dayBuilder.add(day, map.get(month).get(day));
					}
				}
				monthBuilder.add(month, dayBuilder);
			}
		}

		JsonObject jsonObject = monthBuilder.build();
		OutputStream os = new FileOutputStream("src/main/resources/attendance/attendance-" + String.format("%02d", this.getId()) + ".json");
		JsonWriter jsonWriter = Json.createWriter(os);
		HashMap<String, Boolean> config = new HashMap<String, Boolean>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory factory = Json.createWriterFactory(config);
		jsonWriter = factory.createWriter(os);
		jsonWriter.writeObject(jsonObject);
		jsonWriter.close();
	}

	public HashMap<String, HashMap<String, String>> newAttendanceMap() {
		HashMap<String, HashMap<String, String>> map = new HashMap<>();
		for (int month = 1; month < 13; month++) {
			HashMap<String, String> mapDays = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);
			int maxDays = cal.getActualMaximum(Calendar.DATE);
			for (int day = 1; day <= maxDays; day++) {
				cal.set(Calendar.DATE, day);
				int wd = cal.get(Calendar.DAY_OF_WEEK);
				if (wd == 1 || wd == 7) {
					mapDays.put(String.format("%02d", day), "-2");
				} else
					mapDays.put(String.format("%02d", day), "-1");
			}
			map.put(String.format("%02d", month), mapDays);
		}
		return map;
	}

	public void attendanceMapUpdate() throws FileNotFoundException {
		Date start = this.getContract().getStartDate();
		Date d = Calendar.getInstance().getTime();
		LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		HashMap<String, HashMap<String, String>> map = this.getAttendanceMap();
		while (startDate.isBefore(currDate)) {
			String month = startDate.toString().split("-")[1];
			String day = startDate.toString().split("-")[2];
			HashMap<String, String> mapMonth = map.get(month);
			if (mapMonth.get(day).equals("-1")) {
				mapMonth.put(day, "0");
				System.out.println(-1);
			}
			map.put(month, mapMonth);
			this.setAttendanceMap(map);
			startDate = startDate.plusDays(1);

		}

	}

	public void deleteAttendanceMap() {
		File map = new File("src/main/resources/attendance/attendance-" + String.format("%02d", this.getId()) + ".json");
		map.delete();
	}

	public boolean attended() {
		int date = Dates.currDate();
		int month = Dates.currMonth();
		HashMap<String, HashMap<String, String>> map = this.getAttendanceMap();
		String attendance = map.get(String.format("%02d", month)).get(String.format("%02d", date));
		if (attendance.equals("1"))
			return true;
		else
			return false;
	}

	public boolean contracted() {
		if (this.getContract() == null)
			return false;
		else
			return true;
	}

	public long getSalary(String month) {
		long dailyWage = this.getContract().getDailyWage();
		long salary = 0;
		HashMap<String, String> map = this.getAttendanceMap().get(month);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.valueOf(month)-1);
		int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= maxDays; i++) {
			String date = String.format("%02d", i);
			String factor = map.get(date);
			if (factor.equals("1"))
				salary += dailyWage;
			else
			if (factor.equals("0.5"))
				salary += dailyWage * 0.5;
			else
				salary += 0;
		}
		return salary;
	}
}