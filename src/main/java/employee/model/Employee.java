package employee.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import lombok.extern.java.Log;

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

	@Transient
	private HashMap<String, HashMap<String, String>> attendanceMap;

	public HashMap<String, HashMap<String, String>> getAttendanceMap() {
		try {
			InputStream fis = new FileInputStream("src/main/resources/attendance/" + String.format("%02d", this.getId())
					+ "_" + this.getFirstName() + "_" + this.getLastName() + ".json");
			JsonReader jsonReader = Json.createReader(fis);
			JsonObject jsonObject = jsonReader.readObject();
			
			jsonReader.close();
			fis.close();

			HashMap<String, HashMap<String, String>> monthMap = new HashMap<String, HashMap<String, String>>();
			for (int i = 1; i < 13; i++) {
				String month = String.valueOf(i);
				JsonObject obj = jsonObject.getJsonObject(month);
				
				if (obj != null) {
					
					HashMap<String, String> dayMap = new HashMap<>();
					for (int k = 1; k < 32; k++) {
						String day = String.valueOf(k);
						
						if (obj.getString(day) != null) {
//							System.out.println("KLJNHBCGFDXRCTBYUNIJHBGFDSEXRDCTRFBGYUHYN");
							dayMap.put(day, obj.getString(day));
						}
						
					}
					monthMap.put(month, dayMap);
				}
			}
			return monthMap;
		} catch (Exception e) {
//			System.out.println(e);
			return new HashMap<String, HashMap<String,String>>();
		}
	}

	public void setAttendanceMap(HashMap<String, HashMap<String, String>> map) throws FileNotFoundException {
		JsonObjectBuilder monthBuilder = Json.createObjectBuilder();

		for (int i = 1; i < 13; i++) {
			String month = String.valueOf(i);
			if (map.get(month) != null) {
				JsonObjectBuilder dayBuilder = Json.createObjectBuilder();
				for (int k = 1; k < 32; k++) {
					String day = String.valueOf(k);
					if (map.get(month).get(day) != null) {
						dayBuilder.add(day, "1");
					}
				}
				monthBuilder.add(month, dayBuilder);
			}
		}

		JsonObject jsonObject = monthBuilder.build();

		OutputStream os = new FileOutputStream("src/main/resources/attendance/" + String.format("%02d", this.getId())
		+ "_" + this.getFirstName() + "_" + this.getLastName() + ".json");
		JsonWriter jsonWriter = Json.createWriter(os);
		HashMap<String, Boolean> config = new HashMap<String, Boolean>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory factory = Json.createWriterFactory(config);
		jsonWriter = factory.createWriter(os);
		jsonWriter.writeObject(jsonObject);
		jsonWriter.close();
	}

}