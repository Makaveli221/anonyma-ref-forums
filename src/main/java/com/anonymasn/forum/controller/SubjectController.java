package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.dao.filter.SubjectDefaultFields;
import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Typesubject;

import com.anonymasn.forum.payload.request.SubjectRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.SubjectService;
import com.anonymasn.forum.service.TypesubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/subject")
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

	@GetMapping("/type/name/{name}")
	public ResponseEntity<?> getTypeSubjectByName(@PathVariable(value = "name") String name) {
		Optional<Typesubject> typeSubject = typeSubjectService.findByName(name);
		logger.debug("Get TypeSubject by name.");
		
		return ResponseEntity.status(HttpStatus.OK).body(typeSubject);
	}

	@PostMapping("/type/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addTypeSubject(@Valid @RequestBody Typesubject typeSub) {
		if (typeSubjectService.existsByName(typeSub.getName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: Type subject already exist."));
		}
		
		logger.debug("Creating a type Subject.");
		Typesubject newTypeSub = typeSubjectService.create(typeSub);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newTypeSub);
	}

	@PutMapping("/type/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTypeSubject(@PathVariable(value = "id") String id) {
		logger.debug("Getting a type subject for delete.");
		Optional<Typesubject> currTypeSub = typeSubjectService.findById(id);
		if (!currTypeSub.isPresent()) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		
		typeSubjectService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Type subject delete successfully"));
	}

	@GetMapping("/all")
	public ResponseEntity<?> AllSubject(@RequestParam Map<String, String> customQuery) {
		int page = 0;
		int size = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			size = Integer.parseInt(customQuery.get("limit"));
		}
		Page<Subject> subjects = subjectService.getAll(page, size);
		logger.debug("Get all Subjects.");
    return ResponseEntity.status(HttpStatus.OK).body(subjects);
	}

	@GetMapping("/typesubject/{name}")
	public ResponseEntity<?> AllSubjectByTypeSubject(@PathVariable(value = "name") String name, @RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
		Page<Subject> subjects = subjectService.findByTypeSubject(name, page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(subjects);
	}

	@GetMapping("/typesubject/{name}/default")
	public ResponseEntity<?> AllSubjectDefaultByTypeSubject(@PathVariable(value = "name") String name) {
		Collection<SubjectDefaultFields> subjects = subjectService.findByTypeSubject(name);
		return ResponseEntity.status(HttpStatus.OK).body(subjects);
	}

	@GetMapping("/access/{access}")
	public ResponseEntity<?> findByTypeSubjectWithPublicType(@PathVariable(value = "access") int access) {
		boolean publicType = access == 1 ? true : false;
		Collection<Subject> subjects = subjectService.findByTypeSubjectWithPublicType(publicType);
		return ResponseEntity.status(HttpStatus.OK).body(subjects);
	}

	@GetMapping("/{key}")
	public ResponseEntity<?> singleSubject(@PathVariable(value = "key") String key) {
		Optional<Subject> subject = subjectService.findByKey(key);
		logger.debug("Get Subject by key.");
		
		return ResponseEntity.status(HttpStatus.OK).body(subject);
	}

	@GetMapping("/home/list")
	public ResponseEntity<?> homeListSubject(@PathVariable(value = "key") String key) {
		Optional<Subject> subject = subjectService.findByKey(key);
		logger.debug("Get Subject by key.");

		return ResponseEntity.status(HttpStatus.OK).body(subject);
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addSubject(@Valid @RequestBody SubjectRequest subRequest) {
		Subject newSubject = subjectService.create(subRequest);
		if (newSubject == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newSubject);
	}

	@PutMapping("/update/{key}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateSubject(@PathVariable(value = "key") String key, @Valid @RequestBody SubjectRequest subRequest) {
		logger.debug("Get subject for update.");
		Subject updateSub = subjectService.update(key, subRequest);
		if (updateSub == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject or Type Subject not found"));
		}
		logger.debug("Updating Subject.");
		return ResponseEntity.status(HttpStatus.OK).body(updateSub);
	}

	@DeleteMapping("delete/{key}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteSubject(@PathVariable(value = "key") String key) {
		logger.debug("Get Subject for delete.");
		Optional<Subject> currSub = subjectService.findByKey(key);
		if (!currSub.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject not found"));
		}
		subjectService.delete(currSub.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Subject delete successfully"));
	}
}