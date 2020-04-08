package com.anonymasn.forum.controller;

import javax.validation.Valid;

import com.anonymasn.forum.model.Comment;

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
@RequestMapping("/comment")
public class CommentController {

	@GetMapping("/all")
	public ResponseEntity<?> AllComment() {
    return ResponseEntity.status(HttpStatus.OK).body(new String("Get all Comment."));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> singleComment(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Get Comment by id."));
	}

	@PostMapping("/add")
	public ResponseEntity<?> addComment(@Valid @RequestBody Comment sub) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new String("Add new Comment"));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateComment(@PathVariable(value = "id") String id, @Valid @RequestBody Comment sub) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Update Comment"));
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(new String("Delete Comment"));
	}
}