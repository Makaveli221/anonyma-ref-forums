package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Typesubject;

import com.anonymasn.forum.payload.request.SubjectRequest;

import com.anonymasn.forum.service.SubjectService;
import com.anonymasn.forum.service.TypesubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subject")
@PreAuthorize("hasRole('ADMIN')")
public class SubjectController {

	@Autowired
	SubjectService subjectService;

	@Autowired
	TypesubjectService typeSubjectService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/type/all")
	public ResponseEntity<?> AllTypeSubject() {
		Collection<Typesubject> typeSubjects = typeSubjectService.getAll();
		logger.debug("Get all Subject.");
		
		return ResponseEntity.status(HttpStatus.OK).body(typeSubjects);
	}

	@GetMapping("/type/{id}")
	public ResponseEntity<?> getTypeSubjectById(@PathVariable(value = "id") String id) {
		Optional<Typesubject> typeSubject = typeSubjectService.findById(id);
		logger.debug("Get TypeSubject by id.");
		
		return ResponseEntity.status(HttpStatus.OK).body(typeSubject);
	}

	@PostMapping("/type/add")
	public ResponseEntity<?> addTypeSubject(@Valid @RequestBody Typesubject typeSub) {
		if (typeSubjectService.existsByName(typeSub.getName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body("Type subject already exist.");
		}
		
		logger.debug("Creating a type Subject.");
		Typesubject newTypeSub = typeSubjectService.create(typeSub);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newTypeSub);
	}

	@PutMapping("/type/update/{id}")
	public ResponseEntity<?> updateTypeSubject(@PathVariable(value = "id") String id, @Valid @RequestBody Typesubject typeSub) {
		logger.debug("Getting a type subject for update.");
		Optional<Typesubject> currTypeSub = typeSubjectService.findById(id);
		if (!currTypeSub.isPresent()) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		
		typeSub.setId(id);
		Typesubject updateTypeSub = typeSubjectService.update(typeSub);
		logger.debug("Updating a type subject.");
		
		return ResponseEntity.status(HttpStatus.OK).body(updateTypeSub);
	}

	@DeleteMapping("/type/delete/{id}")
	public ResponseEntity<?> deleteTypeSubject(@PathVariable(value = "id") String id) {
		logger.debug("Getting a type subject for delete.");
		Optional<Typesubject> currTypeSub = typeSubjectService.findById(id);
		if (!currTypeSub.isPresent()) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		
		typeSubjectService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Type subject delete successfully");
	}

  @GetMapping("/all")
	public ResponseEntity<?> AllSubject() {
		Collection<Subject> subjects = subjectService.getAll();
		logger.debug("Get all Subjects.");
    return ResponseEntity.status(HttpStatus.OK).body(subjects);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> singleSubject(@PathVariable(value = "id") String id) {
		Optional<Subject> subject = subjectService.findById(id);
		logger.debug("Get Subject by id.");
		
		return ResponseEntity.status(HttpStatus.OK).body(subject);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addSubject(@Valid @RequestBody SubjectRequest subRequest) {
		logger.debug("Get type subject for update.");		

		Subject newSubject = subjectService.create(subRequest);

		if (newSubject == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Type subject not found");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(newSubject);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateSubject(@PathVariable(value = "id") String id, @Valid @RequestBody SubjectRequest subRequest) {
		logger.debug("Get subject for update.");
		
		Subject updateSub = subjectService.update(id, subRequest);
		if (updateSub == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject or Type Subject not found");
		}

		logger.debug("Updating Subject.");
		return ResponseEntity.status(HttpStatus.OK).body(updateSub);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteSubject(@PathVariable(value = "id") String id) {
		logger.debug("Get Subject for delete.");
		Optional<Subject> currSub = subjectService.findById(id);
		if (!currSub.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject not found");
		}
		
		subjectService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Subject delete successfully");
	}
}