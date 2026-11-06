package it.polito.med;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import static java.util.stream.Collectors.*;
import java.util.ArrayList;
import static java.util.Comparator.*;


public class MedManager {
	Set<String> specialtiesColl = new TreeSet<>();
	Map<String, Doctor> doctorColl = new HashMap<>();
	List<Schedule> scheduleColl = new ArrayList<>();
	Map<String, Appointment> appointmentColl = new HashMap<>();
	int appointmentId = 0;
	Map<String, Patient> patientColl = new HashMap<>();
	String currentDate;
	
//	public String increaseAppointmentId() {
//		int id = Integer.parseInt(appointmentId);
//		id++;
//		appointmentId = String.valueOf(id);
//		return appointmentId;
//	}
	
	/**
	 * add a set of medical specialities to the list of specialities
	 * offered by the med centre.
	 * Method can be invoked multiple times.
	 * Possible duplicates are ignored.
	 * 
	 * @param specialities the specialities
	 */
	public void addSpecialities(String... specialities) {
		specialtiesColl.addAll(Arrays.stream(specialities).collect(toSet()));
	}

	/**
	 * retrieves the list of specialities offered in the med centre
	 * 
	 * @return list of specialities
	 */
	public Collection<String> getSpecialities() {
		return specialtiesColl;
	}
	
	
	/**
	 * adds a new doctor with the list of their specialities
	 * 
	 * @param id		unique id of doctor
	 * @param name		name of doctor
	 * @param surname	surname of doctor
	 * @param speciality speciality of the doctor
	 * @throws MedException in case of duplicate id or non-existing speciality
	 */
	public void addDoctor(String id, String name, String surname, String speciality) throws MedException {
		if(doctorColl.containsKey(id))
			throw new MedException("Doctor already exists");
		if(!specialtiesColl.contains(speciality))
			throw new MedException("Specialty doesn't exist");
		Doctor d = new Doctor(id, name, surname, speciality);
		doctorColl.put(id, d);
		
	}

	/**
	 * retrieves the list of doctors with the given speciality
	 * 
	 * @param speciality required speciality
	 * @return the list of doctor ids
	 */
	public Collection<String> getSpecialists(String speciality) {
		return doctorColl.values().stream()
						.filter(d->d.getSpeciality().equals(speciality))
						.map(Doctor::getId)
						.collect(toList())
						;
	}

	/**
	 * retrieves the name of the doctor with the given code
	 * 
	 * @param code code id of the doctor 
	 * @return the name
	 */
	public String getDocName(String code) {
		return doctorColl.get(code).getName();
	}

	/**
	 * retrieves the surname of the doctor with the given code
	 * 
	 * @param code code id of the doctor 
	 * @return the surname
	 */
	public String getDocSurname(String code) {
		return doctorColl.get(code).getSurname();
	}

	/**
	 * Define a schedule for a doctor on a given day.
	 * Slots are created between start and end hours with a 
	 * duration expressed in minutes.
	 * 
	 * @param code	doctor id code
	 * @param date	date of schedule
	 * @param start	start time
	 * @param end	end time
	 * @param duration duration in minutes
	 * @return the number of slots defined
	 */
	public int addDailySchedule(String code, String date, String start, String end, int duration) {
		Doctor d = doctorColl.get(code);
		Schedule s = new Schedule(d, date, start, end, duration);
		scheduleColl.add(s);
//		d.addSchedule(s);
		return s.getNumSlots();
	}

	/**
	 * retrieves the available slots available on a given date for a speciality.
	 * The returned map contains an entry for each doctor that has slots scheduled on the date.
	 * The map contains a list of slots described as strings with the format "hh:mm-hh:mm",
	 * e.g. "14:00-14:30" describes a slot starting at 14:00 and lasting 30 minutes.
	 * 
	 * @param date			date to look for
	 * @param speciality	required speciality
	 * @return a map doc-id -> list of slots in the schedule
	 */
	public Map<String, List<String>> findSlots(String date, String speciality) {
//		Map<String, List<String>> thing = scheduleColl.stream()
//										.filter(s->s.getDate().equals(date))
//										.filter(s->s.getDoctor().getSpeciality().equals(speciality))
//										.collect(
//												groupingBy(
//														s->s.getDoctor().getId(),
//														mapping(
//																Schedule::getListOfSlots,
//																toList()
//														)
//												)
//										)
//										.entrySet().stream()
//										.map(e->Map.entry(e->e.getKey(), e->e.getValue().index(0)))
//										.collect(toMap(
//												e->e.getKey(),
//												e->e.getValue()
//										))
//										;
		Map<String, List<String>> thing = new HashMap<>();
		List<Schedule> interesting = scheduleColl.stream()
										.filter(s->s.getDate().equals(date))
										.filter(s->s.getDoctor().getSpeciality().equals(speciality))
										.collect(toList());
		for(Schedule s : interesting) {
			thing.put(s.getDoctor().getId(), s.getListOfSlots());
		}
		
		return thing;
	}

	/**
	 * Define an appointment for a patient in an existing slot of a doctor's schedule
	 * 
	 * @param ssn		ssn of the patient
	 * @param name		name of the patient
	 * @param surname	surname of the patient
	 * @param code		code id of the doctor
	 * @param date		date of the appointment
	 * @param slot		slot to be booked
	 * @return a unique id for the appointment
	 * @throws MedException	in case of invalid code, date or slot
	 */
	public String setAppointment(String ssn, String name, String surname, String code, String date, String slot) throws MedException {
		if(!doctorColl.containsKey(code))
			throw new MedException("Doctor not found");
		if(scheduleColl.stream().map(Schedule::getDate).filter(s->s.equals(date)).count()<1)
			throw new MedException("Doesn't exist a schedule for that date");
	
		if(
				!scheduleColl.stream().filter(s->s.getDate().equals(date))
									.filter(s->s.getDoctor().getId().equals(code))
									.flatMap(s->s.getListOfSlots().stream())
									.collect(toList()).contains(slot)
				)
			throw new MedException("Slot provided is invalid");
		
		appointmentId++;
		Patient p = patientColl.computeIfAbsent(ssn, key -> new Patient(ssn, name, surname));
		patientColl.put(ssn, p);
		Appointment a = new Appointment(appointmentId, p, doctorColl.get(code), date, slot);
		//add appointment to schedule
		scheduleColl.stream().filter(s->s.getDate().equals(date))
		.filter(s->s.getDoctor().getId().equals(code)).findFirst().get().addAppointment(a);
		
		//add appoinment to general coll
		appointmentColl.put(String.valueOf(appointmentId), a);
		return String.valueOf(appointmentId);
	}

	/**
	 * retrieves the doctor for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return doctor code id
	 */
	public String getAppointmentDoctor(String idAppointment) {
		return appointmentColl.get(idAppointment).getDoctor().getId();
	}

	/**
	 * retrieves the patient for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return doctor patient ssn
	 */
	public String getAppointmentPatient(String idAppointment) {
		return appointmentColl.get(idAppointment).getPatient().getSsn();
	}

	/**
	 * retrieves the time for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return time of appointment
	 */
	public String getAppointmentTime(String idAppointment) {
		return appointmentColl.get(idAppointment).getSlot().split("-")[0];
	}

	/**
	 * retrieves the date for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return date
	 */
	public String getAppointmentDate(String idAppointment) {
		return appointmentColl.get(idAppointment).getDate();
	}

	/**
	 * retrieves the list of a doctor appointments for a given day.
	 * Appointments are reported as string with the format
	 * "hh:mm=SSN"
	 * 
	 * @param code doctor id
	 * @param date date required
	 * @return list of appointments
	 */
	public Collection<String> listAppointments(String code, String date) {
		return appointmentColl.values().stream()
									.filter(a->a.getDoctor().getId().equals(code))
									.filter(a->a.getDate().equals(date))
									.map(Appointment::toString)
									.collect(toList())
									;
	}

	/**
	 * Define the current date for the medical centre
	 * The date will be used to accept patients arriving at the centre.
	 * 
	 * @param date	current date
	 * @return the number of total appointments for the day
	 */
	public int setCurrentDate(String date) {
		currentDate = date;
		return (int) appointmentColl.values().stream()
			 					.filter(a->a.getDate().equals(date))
								.count();
	}

	/**
	 * mark the patient as accepted by the med centre reception
	 * 
	 * @param ssn SSN of the patient
	 */
	public void accept(String ssn) {
		appointmentColl.values().stream()
								.filter(a->a.getPatient().getSsn().equals(ssn))
								.sorted(comparing(Appointment::getDate))
								.limit(1)
								.forEach(a->a.accepted=true);
	}

	/**
	 * returns the next appointment of a patient that has been accepted.
	 * Returns the id of the earliest appointment whose patient has been
	 * accepted and the appointment not completed yet.
	 * Returns null if no such appointment is available.
	 * 
	 * @param code	code id of the doctor
	 * @return appointment id
	 */
	public String nextAppointment(String code) {
		return appointmentColl.values().stream()
			    .filter(a -> a.accepted)
			    .filter(a -> !a.completed)
			    .sorted(comparing(Appointment::getDate).thenComparing(Appointment::getSlot))
			    .map(Appointment::getAppointmentId)
			    .findFirst()
			    .map(String::valueOf)
			    .orElse(null);

	}

	/**
	 * mark an appointment as complete.
	 * The appointment must be with the doctor with the given code
	 * the patient must have been accepted
	 * 
	 * @param code		doctor code id
	 * @param appId		appointment id
	 * @throws MedException in case code or appointment code not valid,
	 * 						or appointment with another doctor
	 * 						or patient not accepted
	 * 						or appointment not for the current day
	 */
	public void completeAppointment(String code, String appId)  throws MedException {
		if(!doctorColl.containsKey(code))
			throw new MedException("Doctor doesn't exist");
		if(!appointmentColl.containsKey(appId))
			throw new MedException("Appointment doesn't exist");
		if(!appointmentColl.get(appId).getDoctor().getId().equals(code))
			throw new MedException("Appointment with another doctor");
		if(!appointmentColl.get(appId).accepted || !appointmentColl.get(appId).getDate().equals(currentDate))
			throw new MedException("Patient not accepted");
		appointmentColl.get(appId).completed = true;
	}

	/**
	 * computes the show rate for the appointments of a doctor on a given date.
	 * The rate is the ratio of accepted patients over the number of appointments
	 *  
	 * @param code		doctor id
	 * @param date		reference date
	 * @return	no show rate
	 */
	public double showRate(String code, String date) {
		return appointmentColl.values().stream()
                .filter(a -> a.getDoctor().getId().equals(code))
                .filter(a -> a.getDate().equals(date))
                .collect(collectingAndThen(
                        partitioningBy(a->a.accepted),
                        m -> (double) m.get(true).size() / (m.get(true).size() + m.get(false).size())
                ));

	}

	/**
	 * computes the schedule completeness for all doctors of the med centre.
	 * The completeness for a doctor is the ratio of the number of appointments
	 * over the number of slots in the schedule.
	 * The result is a map that associates to each doctor id the relative completeness
	 * 
	 * @return the map id : completeness
	 */
	public Map<String, Double> scheduleCompleteness() {
		return doctorColl.keySet().stream()
		        .collect(toMap(
		                doctorId -> doctorId,
		                doctorId -> {
		                    Long appointments = Optional.ofNullable(appointmentColl.values().stream()
		                            .filter(a -> a.getDoctor().getId().equals(doctorId))
		                            .count())
		                            .orElse(0L);
		                    Long slots = Optional.ofNullable(scheduleColl.stream()
		                            .filter(s -> s.getDoctor().getId().equals(doctorId))
		                            .mapToLong(Schedule::getNumSlots)
		                            .sum())
		                            .orElse(0L);
		                    return slots == 0 ? Double.NaN : appointments.doubleValue() / slots;
		                }
		        ));
	}
	
}
