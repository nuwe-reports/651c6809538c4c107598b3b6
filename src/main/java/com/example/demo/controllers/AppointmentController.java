package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Appointment;
import com.example.demo.repositories.AppointmentRepository;

@RestController
@RequestMapping("/api")
public class AppointmentController {

	@Autowired
	AppointmentRepository appointmentRepository;

	@GetMapping("/appointments")
	public ResponseEntity<List<Appointment>> getAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();

		appointmentRepository.findAll().forEach(appointments::add);

		if (appointments.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(appointments, HttpStatus.OK);
	}

	@GetMapping("/appointments/{id}")
	public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
		Optional<Appointment> appointment = appointmentRepository.findById(id);

		if (appointment.isPresent()) {
			return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}	
	
	@PostMapping("/appointment")
	public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment) {
	    try {
	        if (invalidAppointment(appointment)) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }

	        Appointment newAppointment = createNewAppointment(appointment);

	        if (hasOverlap(newAppointment)) {
	            return new ResponseEntity<>( HttpStatus.NOT_ACCEPTABLE);
	        }

	        appointmentRepository.save(newAppointment);
	        return new ResponseEntity<>(HttpStatus.OK);
	    } catch (Exception ex) {
	        return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private boolean invalidAppointment(Appointment appointment) {
	    return appointment.getRoom() == null || 
	           appointment.getDoctor() == null ||
	           appointment.getStartsAt() == null ||
	           appointment.getFinishesAt() == null ||
	           appointment.getStartsAt().isEqual(appointment.getFinishesAt());
	}

	private Appointment createNewAppointment(Appointment appointment) {
	    return new Appointment(appointment.getPatient(), 
	                           appointment.getDoctor(), 
	                           appointment.getRoom(),
	                           appointment.getStartsAt(),
	                           appointment.getFinishesAt());
	}

	private boolean hasOverlap(Appointment newAppointment) {
	    List<Appointment> appointmentList = appointmentRepository.findAll();
	    return appointmentList.stream()
	                          .anyMatch(existingAppointment -> newAppointment.overlaps(existingAppointment));
	}

	@DeleteMapping("/appointments/{id}")
	public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {

		Optional<Appointment> appointment = appointmentRepository.findById(id);

		if (!appointment.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		appointmentRepository.deleteById(id);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@DeleteMapping("/appointments")
	public ResponseEntity<HttpStatus> deleteAllAppointments() {
		appointmentRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
