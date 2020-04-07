package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.TopicService;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	TopicService topicService;

  @GetMapping("/all")
	public ResponseEntity<?> AllTopic() {
    Collection<Topic> topics = topicService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(topics);
	}

	@GetMapping("/subject/{id}")
	public ResponseEntity<?> AllTopicBySubject(@PathVariable(value = "id") String id) {
		Collection<Topic> topics = topicService.findBySubject(id);
		return ResponseEntity.status(HttpStatus.OK).body(topics);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> singleTopic(@PathVariable(value = "id") String id) {
		Optional<Topic> topic = topicService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(topic);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addTopic(@Valid @RequestBody TopicRequest topRequest) {
		Topic newTopic = topicService.create(topRequest);
		if (newTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newTopic);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateTopic(@PathVariable(value = "id") String id, @Valid @RequestBody TopicRequest topRequest) {
		Topic updateTopic = topicService.update(id, topRequest);
		if (updateTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject or Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateTopic);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteTopic(@PathVariable(value = "id") String id) {
		Optional<Topic> currTopic = topicService.findById(id);
		if (!currTopic.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found or already deleted."));
		}
		topicService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Topic is successfully deleted"));
	}
}