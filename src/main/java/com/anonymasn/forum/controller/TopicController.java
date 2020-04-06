package com.anonymasn.forum.controller;

import javax.validation.Valid;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic")
public class TopicController {

  @GetMapping("/all")
	public ResponseEntity<?> AllTopic() {
    return ResponseEntity.status(HttpStatus.OK).body(new String("Get all Topic."));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> singleTopic(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Get Topic by id."));
	}

	@PostMapping("/add")
	public ResponseEntity<?> addTopic(@Valid @RequestBody TopicRequest topRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new String("Add new Topic"));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateTopic(@PathVariable(value = "id") String id, @Valid @RequestBody Topic top) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Update Topic"));
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteTopic(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Delete Topic"));
	}
}