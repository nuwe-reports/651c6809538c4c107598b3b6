package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    
    /** TODO
     * Implement tests for each Entity class: Doctor, Patient, Room and Appointment.
     * Make sure you are as exhaustive as possible. Coverage is checked ;)
     */
    
    @Test
	void testDoctorEntityPersistence() {
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");

		entityManager.persist(d1);

		Doctor retrievedDoctor = entityManager.find(Doctor.class, d1.getId());

		assertThat(retrievedDoctor).isNotNull();
		assertThat(retrievedDoctor.getFirstName()).isEqualTo("Marcos");
		assertThat(retrievedDoctor.getLastName()).isEqualTo("Corporan");
		assertThat(retrievedDoctor.getAge()).isEqualTo(28);
		assertThat(retrievedDoctor.getEmail()).isEqualTo("Mcorporan@hospital.com");
	}

	@Test
	void testDoctorEntityNull() {
		d1 = new Doctor();
		entityManager.persist(d1);
		Doctor retrievedDoctorNull = entityManager.find(Doctor.class, d1.getId());

		assertThat(retrievedDoctorNull).isNotNull();
		assertThat(retrievedDoctorNull.getFirstName()).isNull();
		assertThat(retrievedDoctorNull.getLastName()).isNull();
		assertThat(retrievedDoctorNull.getAge()).isEqualTo(0);
		assertThat(retrievedDoctorNull.getEmail()).isNull();
	}

	// Patient
	@Test
	void testPatientEntityPersistence() {
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");
		entityManager.persist(p1);

		Patient retrievedPatient = entityManager.find(Patient.class, p1.getId());

		assertThat(retrievedPatient).isNotNull();
		assertThat(retrievedPatient.getFirstName()).isEqualTo("Juana");
		assertThat(retrievedPatient.getLastName()).isEqualTo("Sosa");
		assertThat(retrievedPatient.getAge()).isEqualTo(53);
		assertThat(retrievedPatient.getEmail()).isEqualTo("jsosa@xmail.com");
	}

	@Test
	void testPatientEntityNull() {
		p1 = new Patient();
		entityManager.persist(p1);
		Patient retrievedPatientNull = entityManager.find(Patient.class, p1.getId());

		assertThat(retrievedPatientNull).isNotNull();
		assertThat(retrievedPatientNull.getFirstName()).isNull();
		assertThat(retrievedPatientNull.getLastName()).isNull();
		assertThat(retrievedPatientNull.getAge()).isEqualTo(0);
		assertThat(retrievedPatientNull.getEmail()).isNull();
	}

	// Room
	@Test
	void testRoomEntityPersistence() {
		r1 = new Room("gynecology");

		entityManager.persist(r1);

		Room retrievedRoom = entityManager.find(Room.class, r1.getRoomName());

		assertThat(retrievedRoom).isNotNull();
		assertThat(retrievedRoom.getRoomName()).isEqualTo("gynecology");
	}

	// Appointment
	@Test
	void testAppointmentEntityPersistence() {
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");
		r1 = new Room("gynecology");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAt = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAt = LocalDateTime.parse("20:30 22/10/2023", formatter);

		a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);

		entityManager.persist(a1);

		Appointment retrievedAppointment = entityManager.find(Appointment.class, a1.getId());

		assertThat(retrievedAppointment).isNotNull();
		assertThat(retrievedAppointment.getPatient()).isEqualTo(p1);
		assertThat(retrievedAppointment.getDoctor()).isEqualTo(d1);
		assertThat(retrievedAppointment.getRoom()).isEqualTo(r1);
		assertThat(retrievedAppointment.getStartsAt()).isEqualTo(startsAt);
		assertThat(retrievedAppointment.getFinishesAt()).isEqualTo(finishesAt);

	}

	@Test
	void testAppointmentEntityPersistenceNull() {
		a1 = new Appointment();

		entityManager.persist(a1);

		Appointment retrievedAppointment = entityManager.find(Appointment.class, a1.getId());

		assertThat(retrievedAppointment).isNotNull();
		assertThat(retrievedAppointment.getPatient()).isNull();
		assertThat(retrievedAppointment.getDoctor()).isNull();
		assertThat(retrievedAppointment.getRoom()).isNull();
		assertThat(retrievedAppointment.getStartsAt()).isNull();
		assertThat(retrievedAppointment.getFinishesAt()).isNull();

	}

	@Test
	void testValidDateTimeFormat() {

		// String validDateTimeString = "23:59 22/10/2023";
		LocalDateTime validDateTime = LocalDateTime.of(2023, 10, 22, 23, 59);

		Appointment appointment = new Appointment();
		appointment.setStartsAt(validDateTime);
		appointment.setFinishesAt(validDateTime);

		LocalDateTime retrievedStartsAt = appointment.getStartsAt();
		LocalDateTime retrievedFinishesAt = appointment.getFinishesAt();

		assertThat(retrievedStartsAt).isNotNull();
		assertThat(retrievedFinishesAt).isNotNull();
		assertThat(retrievedStartsAt).isEqualTo(validDateTime);
		assertThat(retrievedFinishesAt).isEqualTo(validDateTime);

	}

	@Test
	void testAppointmentRelationship() {
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");
		r1 = new Room("gynecology");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAt = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAt = LocalDateTime.parse("20:30 22/10/2023", formatter);

		a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);

		assertThat(a1).isNotNull();
		assertThat(a1.getPatient()).isEqualTo(p1);
		assertThat(a1.getDoctor()).isEqualTo(d1);
		assertThat(a1.getRoom()).isEqualTo(r1);
		assertThat(a1.getStartsAt()).isEqualTo(startsAt);
		assertThat(a1.getFinishesAt()).isEqualTo(finishesAt);

	}

	@Test
	void testAppointmentOverlapsCase1() {
		// Case 1: A.starts == B.starts
		
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");		
		r1 = new Room("gynecology");


		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAtA = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAtA = LocalDateTime.parse("20:30 22/10/2023", formatter);
		LocalDateTime startsAtB = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAtB = LocalDateTime.parse("21:00 22/10/2023", formatter);

		a1 = new Appointment(p1, d1, r1, startsAtA, finishesAtA);
		a2 = new Appointment(p1, d1, r1, startsAtB, finishesAtB);

		assertTrue(a1.overlaps(a2));
	}

	@Test
	void testAppointmentOverlapsCase2() {
		// Case 2: A.finishes == B.finishes
		
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");
		r1 = new Room("gynecology");


		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAtA = LocalDateTime.parse("19:00 22/10/2023", formatter);
		LocalDateTime finishesAtA = LocalDateTime.parse("20:30 22/10/2023", formatter);
		LocalDateTime startsAtB = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAtB = LocalDateTime.parse("20:30 22/10/2023", formatter);

		a1 = new Appointment(p1, d1, r1, startsAtA, finishesAtA);
		a2 = new Appointment(p1, d1, r1, startsAtB, finishesAtB);

		assertTrue(a1.overlaps(a2));

	}

	@Test
	void testAppointmentOverlapsCase3() {
		// Case 3: A.starts < B.finishes && B.finishes < A.finishes
		
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");
		r1 = new Room("gynecology");

		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAtA = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAtA = LocalDateTime.parse("20:30 22/10/2023", formatter);
		LocalDateTime startsAtB = LocalDateTime.parse("20:00 22/10/2023", formatter);
		LocalDateTime finishesAtB = LocalDateTime.parse("20:15 22/10/2023", formatter);
		
		a1 = new Appointment(p1, d1, r1, startsAtA, finishesAtA);
		a2 = new Appointment(p1, d1, r1, startsAtB, finishesAtB);
		
		assertTrue(a1.overlaps(a2));

	}

	@Test
	void testAppointmentOverlapsCase4() {
		 // Case 4: B.starts < A.starts && A.finishes < B.finishes
		
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");		
		r1 = new Room("gynecology");

		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAtA = LocalDateTime.parse("19:30 22/10/2023", formatter);
		LocalDateTime finishesAtA = LocalDateTime.parse("20:00 22/10/2023", formatter);
		LocalDateTime startsAtB = LocalDateTime.parse("19:00 22/10/2023", formatter);
		LocalDateTime finishesAtB = LocalDateTime.parse("19:45 22/10/2023", formatter);
		
		a1 = new Appointment(p1, d1, r1, startsAtA, finishesAtA);
		a2 = new Appointment(p1, d1, r1, startsAtB, finishesAtB);
		
		assertTrue(a1.overlaps(a2));
	}

	@Test
	void testAppointmentOverlaps() {
		d1 = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");
		p1 = new Patient("Juana", "Sosa", 53, "jsosa@xmail.com");		
		r1 = new Room("gynecology");

		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		LocalDateTime startsAtA = LocalDateTime.parse("10:00 23/10/2023", formatter);
		LocalDateTime finishesAtA = LocalDateTime.parse("10:30 23/10/2023", formatter);
		LocalDateTime startsAtB = LocalDateTime.parse("11:30 23/10/2023", formatter);
		LocalDateTime finishesAtB = LocalDateTime.parse("12:00 23/10/2023", formatter);
		
		a1 = new Appointment(p1, d1, r1, startsAtA, finishesAtA);
		a2 = new Appointment(p1, d1, r1, startsAtB, finishesAtB);
		
		assertFalse(a1.overlaps(a2));
	}
}
