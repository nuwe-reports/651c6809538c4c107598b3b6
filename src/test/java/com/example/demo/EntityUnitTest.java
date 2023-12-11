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
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

	private Room r1;

	private Appointment a1;
	private Appointment a2;

	@Test
	void testDoctorEntityPersistence() throws Exception {
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
	void testDoctorEntityNull() throws Exception {
		d1 = new Doctor();
		entityManager.persist(d1);
		Doctor retrievedDoctorNull = entityManager.find(Doctor.class, d1.getId());

		assertThat(retrievedDoctorNull).isNotNull();
		assertThat(retrievedDoctorNull.getFirstName()).withFailMessage("First name should be null").isNull();
		assertThat(retrievedDoctorNull.getLastName()).withFailMessage("Last name should be null").isNull();
		assertThat(retrievedDoctorNull.getAge()).isEqualTo(0);
		assertThat(retrievedDoctorNull.getEmail()).withFailMessage("Email should be null").isNull();
	}

	// Patient
	@Test
	void testPatientEntityPersistence() throws Exception {
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
	void testPatientEntityNull() throws Exception {
		p1 = new Patient();
		entityManager.persist(p1);
		Patient retrievedPatientNull = entityManager.find(Patient.class, p1.getId());

		assertThat(retrievedPatientNull).isNotNull();
		assertThat(retrievedPatientNull.getFirstName()).withFailMessage("First name should be null").isNull();
		assertThat(retrievedPatientNull.getLastName()).withFailMessage("Last name should be null").isNull();
		assertThat(retrievedPatientNull.getAge()).isEqualTo(0);
		assertThat(retrievedPatientNull.getEmail()).withFailMessage("Email should be null").isNull();
	}

	// Room
	@Test
	void testRoomEntityPersistence() throws Exception {
		r1 = new Room("gynecology");

		entityManager.persist(r1);

		Room retrievedRoom = entityManager.find(Room.class, r1.getRoomName());

		assertThat(retrievedRoom).isNotNull();
		assertThat(retrievedRoom.getRoomName()).isEqualTo("gynecology");
	}

	@Test
	void testAppointmentEntityPersistence() throws Exception {
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
	void testAppointmentEntityPersistenceNull() throws Exception {
		a1 = new Appointment();

		entityManager.persist(a1);

		Appointment retrievedAppointment = entityManager.find(Appointment.class, a1.getId());

		assertThat(retrievedAppointment).isNotNull();
		assertThat(retrievedAppointment.getPatient()).withFailMessage("Patient should be null").isNull();
		assertThat(retrievedAppointment.getDoctor()).withFailMessage("Doctor should be null").isNull();
		assertThat(retrievedAppointment.getRoom()).withFailMessage("Room should be null").isNull();
		assertThat(retrievedAppointment.getStartsAt()).withFailMessage("StartsAt should be null").isNull();
		assertThat(retrievedAppointment.getFinishesAt()).withFailMessage("FinishesAt should be null").isNull();
	}

	@Test
	void testValidDateTimeFormat() throws Exception {

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
	void testAppointmentRelationship() throws Exception {
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
	void testAppointmentOverlapsCase1() throws Exception {

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
	void testAppointmentOverlapsCase2() throws Exception {

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
	void testAppointmentOverlapsCase3() throws Exception {

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
	void testAppointmentOverlapsCase4() throws Exception {

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
	void testAppointmentOverlaps() throws Exception {
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
