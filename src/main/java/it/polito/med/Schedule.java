package it.polito.med;

import java.util.List;
import java.util.ArrayList;

public class Schedule {
	private Doctor doctor;
	private String date;
	private String start;
	private int startHour;
	private int startMin;
	private String end;
	private int endHour;
	private int endMin;
	private int duration;
	private int slots;
	
	List<Appointment> appointmentColl = new ArrayList<>();
	
	public Schedule(Doctor doctor, String date, String start, String end, int duration) {
		this.doctor = doctor;
		this.date = date;
		this.start = start;
		this.end = end;
		this.duration = duration;
		startHour = Integer.parseInt(start.split(":")[0]);
		startMin = Integer.parseInt(start.split(":")[1]);
		endHour = Integer.parseInt(end.split(":")[0]);
		endMin = Integer.parseInt(end.split(":")[1]);
		slots = ((endHour-startHour)*60+(endMin-startMin))/duration;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public String getDate() {
		return date;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public int getDuration() {
		return duration;
	}

	public int getNumSlots() {
		return slots;
	}
	
	public String gen(int h, int min, int dur) {
		if(min+dur>=60) {
			int f = min+dur-60;
			return String.format("%02d:%02d-%02d:%02d", h, min, h+1, f);
		}
		else {
			return String.format("%02d:%02d-%02d:%02d", h, min, h, min+dur);
		}
	}
	
	public List<String> getListOfSlots(){
		List<String> thing = new ArrayList<>();
		String s;
		int hour = startHour;
		int min = startMin;
		int dur = duration;
		for(int i=0; i<slots; i++) {
			s = gen(hour, min, dur);
			thing.add(s);
			hour = Integer.parseInt((s.split("-")[1]).split(":")[0]);
			min = Integer.parseInt((s.split("-")[1]).split(":")[1]);
		}
		return thing;
	}
		
	public void addAppointment(Appointment a) {
		appointmentColl.add(a);
	}
}
