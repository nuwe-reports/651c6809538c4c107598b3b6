
package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

	@MockBean
	private DoctorRepository doctorRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldGetAllDoctors() throws Exception {
		List<Doctor> doctors = Arrays.asList(new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com"),
				new Doctor("Hector", "De la Rosa", 52, "Hdelarosa@hospital.com"));

		when(doctorRepository.findAll()).thenReturn(doctors);

		mockMvc.perform(get("/api/doctors")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(doctors)));
	}

	@Test
	void shouldGetDoctorById() throws Exception {
		Doctor doctor = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");

		when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

		mockMvc.perform(get("/api/doctors/1")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(doctor)));
	}

	@Test
	void shouldCreateDoctor() throws Exception {
		Doctor doctorToCreate = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");

		mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorToCreate))).andExpect(status().isCreated());
	}

	@Test
	void shouldDeleteDoctorById() throws Exception {

		Doctor existingDoctor = new Doctor("Marcos", "Corporan", 28, "Mcorporan@hospital.com");

		when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

		mockMvc.perform(delete("/api/doctors/1")).andExpect(status().isOk());
	}

	@Test
	void shouldDeleteAllDoctors() throws Exception {
		mockMvc.perform(delete("/api/doctors")).andExpect(status().isOk());
	}
}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

	@MockBean
	private PatientRepository patientRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldGetAllPatients() throws Exception {
		List<Patient> patients = Arrays.asList(new Patient("Juana", "Sosa", 53, "jsosa@hospital.com"),
				new Patient("Albert", "Triviño", 23, "Atrivino@hospital.com"));

		when(patientRepository.findAll()).thenReturn(patients);

		mockMvc.perform(get("/api/patients")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(patients)));
	}

	@Test
	void shouldGetPatientById() throws Exception {
		Patient patient = new Patient("Juana", "Sosa", 53, "jsosa@hospital.com");

		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

		mockMvc.perform(get("/api/patients/1")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(patient)));
	}

	@Test
	void shouldCreatePatient() throws Exception {
		Patient patientToCreate = new Patient("Alice", "Smith", 25, "alice.smith@example.com");

		mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientToCreate))).andExpect(status().isCreated());
	}

	@Test
	void shouldDeletePatientById() throws Exception {

		Patient existingPatient = new Patient("Juana", "Sosa", 53, "jsosa@hospital.com");

		when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

		mockMvc.perform(delete("/api/patients/1")).andExpect(status().isOk());
	}

	@Test
	void shouldDeleteAllPatients() throws Exception {
		mockMvc.perform(delete("/api/patients")).andExpect(status().isOk());
	}
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

	@MockBean
	private RoomRepository roomRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldGetAllRooms() throws Exception {
		List<Room> rooms = Arrays.asList(new Room("gynecology"), new Room("dermatology"));

		when(roomRepository.findAll()).thenReturn(rooms);

		mockMvc.perform(get("/api/rooms")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(rooms)));
	}

	@Test
	void shouldGetRoomByRoomName() throws Exception {
		Room room = new Room("gynecology");

		when(roomRepository.findByRoomName("gynecology")).thenReturn(Optional.of(room));

		mockMvc.perform(get("/api/rooms/gynecology")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(room)));
	}

	@Test
	void shouldCreateRoom() throws Exception {
		Room roomToCreate = new Room("dermatology");

		mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(roomToCreate))).andExpect(status().isCreated());
	}

	@Test
	void shouldDeleteRoomByRoomName() throws Exception {

		Room existingRoom = new Room("dermatology");

		when(roomRepository.findByRoomName("dermatology")).thenReturn(Optional.of(existingRoom));

		mockMvc.perform(delete("/api/rooms/dermatology")).andExpect(status().isOk());
	}

	@Test
	void shouldDeleteAllRooms() throws Exception {
		mockMvc.perform(delete("/api/rooms")).andExpect(status().isOk());
	}
}
