package it.polito.med;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
	private String id;
	private String name;
	private String surname;
	private String speciality;
	private List<Schedule> schedules = new ArrayList<>();
	
	public Doctor(String id, String name, String surname, String speciality) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.speciality = speciality;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getSpeciality() {
		return speciality;
	}
	
	public void addSchedule(Schedule s) {
		schedules.add(s);
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}
	
}
