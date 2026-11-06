package it.polito.med;

public class Appointment {
	private int appointmentId;
	private Patient patient;
	private Doctor doctor;
	private String date;
	private String slot;
	boolean completed=false;
	boolean accepted = false;

	public Appointment(int appointmentId, Patient patient, Doctor doctor, String date, String slot) {
		this.appointmentId = appointmentId;
		this.patient = patient;
		this.doctor = doctor;
		this.date = date;
		this.slot = slot;
	}


	
	public int getAppointmentId() {
		return appointmentId;
	}



	public Patient getPatient() {
		return patient;
	}



	public Doctor getDoctor() {
		return doctor;
	}



	public String getDate() {
		return date;
	}



	public String getSlot() {
		return slot;
	}



	public String toString() {
		return (slot.split("-")[0])+"="+patient.getSsn();
	}
	
	
	
}
