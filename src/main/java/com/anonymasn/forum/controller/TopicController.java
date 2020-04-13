package com.anonymasn.forum.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.TopicService;

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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/topic")
public class TopicController {

	@Autowired
	TopicService topicService;

  	@GetMapping("/all")
	public ResponseEntity<?> AllTopic() {
    Iterable<Topic> topics = topicService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(topics);
	}

	@GetMapping("/subject/{key}")
	public ResponseEntity<?> AllTopicBySubject(@PathVariable(value = "key") String key, @RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 20;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
		Page<Topic> topics = topicService.findBySubject(key, page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(topics);
	}
	
	@GetMapping("/{key}")
	public ResponseEntity<?> singleTopic(@PathVariable(value = "key") String key) {
		Optional<Topic> topic = topicService.findByKey(key);
		return ResponseEntity.status(HttpStatus.OK).body(topic);
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> addTopic(@Valid @RequestBody TopicRequest topRequest) {
		Topic newTopic = topicService.create(topRequest);
		if (newTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newTopic);
	}

	@PutMapping("/update/{key}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> updateTopic(@PathVariable(value = "key") String key, @Valid @RequestBody TopicRequest topRequest) {
		Topic updateTopic = topicService.update(key, topRequest);
		if (updateTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject or Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateTopic);
	}

	@DeleteMapping("delete/{key}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> deleteTopic(@PathVariable(value = "key") String key) {
		Optional<Topic> currTopic = topicService.findByKey(key);
		if (!currTopic.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found or already deleted."));
		}
		topicService.delete(currTopic.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Topic is successfully deleted"));
	}
}