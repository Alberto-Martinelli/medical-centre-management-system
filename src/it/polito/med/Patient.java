package it.polito.med;

public class Patient {
	private String ssn;
	private String name;
	private String surname;
	private boolean accepted;
	
	public Patient(String ssn, String name, String surname) {
		this.ssn = ssn;
		this.name = name;
		this.surname = surname;
	}

	public String getSsn() {
		return ssn;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	
	
}
