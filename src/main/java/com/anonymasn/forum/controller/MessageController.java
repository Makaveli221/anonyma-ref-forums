package com.anonymasn.forum.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.payload.request.CommentRequest;
import com.anonymasn.forum.payload.request.MessageRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.CommentService;
import com.anonymasn.forum.service.MessageService;

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
@RequestMapping("/message")
public class MessageController {
  
  @Autowired
	MessageService messageService;
	
	@Autowired
	CommentService commentService;
  
  @GetMapping("/all/{type}")
	public ResponseEntity<?> filterByType(@PathVariable(value = "type") String type, @RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
    Page<Message> messages = messageService.findByType(type, page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(messages);
  }

  @GetMapping("/{type}/{email}")
	public ResponseEntity<?> filterByTypeAndEmail(@PathVariable(value = "type") String type, @PathVariable(value = "email") String email) {
		Collection<Message> messages = messageService.findByTypeAndEmail(type, email);
		return ResponseEntity.status(HttpStatus.OK).body(messages);
	}
	
	@GetMapping("/history/published")
	public ResponseEntity<?> filterByPublished(@RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
    Page<Message> messages = messageService.findByPublished(page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(messages);
	}

	@GetMapping("filterbyid/{id}")
	public ResponseEntity<?> filterById(@PathVariable(value = "id") String id) {
		Optional<Message> message = messageService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(message);
  }
  
  @PostMapping("/add")
	public ResponseEntity<?> addMessage(@Valid @RequestBody MessageRequest messRequest) {
		Message newMessage = messageService.create(messRequest);
		if (newMessage == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: an error was occured"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
	}

  @PutMapping("/update/{id}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> updateMessage(@PathVariable(value = "id") String id, @Valid @RequestBody MessageRequest messRequest) {
		Message updateMessage = messageService.update(id, messRequest);
		if (updateMessage == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Message not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateMessage);
  }
  
  @DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteMessage(@PathVariable(value = "id") String id) {
		Optional<Message> currMessage = messageService.findById(id);
		if (!currMessage.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Message not found or already deleted."));
		}
		messageService.delete(currMessage.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Message is successfully deleted"));
	}

	@GetMapping("/last")
	public ResponseEntity<?> lastMessages() {
		Collection<Message> messages = messageService.getLastMessages();
		return ResponseEntity.status(HttpStatus.OK).body(messages);
	}

	@GetMapping("/{id}/comments")
	public ResponseEntity<?> listComments(@PathVariable(value = "id") String id) {
		Collection<Comment> comments = commentService.findByHsitoire(id);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}

	@GetMapping("/{id}/comments/count")
	public ResponseEntity<?> countComments(@PathVariable(value = "id") String id) {
		Collection<Comment> comments = commentService.findByHsitoire(id);
		return ResponseEntity.status(HttpStatus.OK).body(comments.stream().count());
	}

	@PostMapping("/comment/add")
	public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commRequest) {
		Comment newComment = commentService.create(commRequest);
		if (newComment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Source not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
	}

	@PutMapping("/comment/update/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> updateComment(@PathVariable(value = "id") String id, @Valid @RequestBody CommentRequest commRequest) {
		Comment updateComment = commentService.update(id, commRequest);
		if (updateComment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Comment not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateComment);
	}

	@DeleteMapping("/comment/delete/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "id") String id) {
		Optional<Comment> currComm = commentService.findById(id);
		if (!currComm.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Comment not found"));
		}
		commentService.delete(currComm.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Comment delete successfully"));
	}
}