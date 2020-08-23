package com.anonymasn.forum.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.CommentRequest;
import com.anonymasn.forum.payload.request.TopicRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.CommentService;
import com.anonymasn.forum.service.TopicService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/topic")
public class TopicController {

	@Autowired
	TopicService topicService;

	@Autowired
	CommentService commentService;

  @GetMapping("/all")
	public ResponseEntity<?> AllTopic() {
    Iterable<Topic> topics = topicService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(topics);
	}

	@GetMapping("/subject/{key}")
	public ResponseEntity<?> AllTopicBySubject(@PathVariable(value = "key") String key, @RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
		Page<Topic> topics = topicService.findBySubject(key, page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(topics);
	}

	@GetMapping("/createuser/{id}")
	public ResponseEntity<?> AllTopicByCreateUser(@PathVariable(value = "id") String id, @RequestParam Map<String, String> customQuery) {
		int page = 0;
		int limit = 10;
		if(customQuery.containsKey("page")) {
			page = Integer.parseInt(customQuery.get("page"));
		}
		if(customQuery.containsKey("limit")) {
			limit = Integer.parseInt(customQuery.get("limit"));
		}
		Page<Topic> topics = topicService.findByCreateUser(id, page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(topics);
	}
	
	@GetMapping("/{key}")
	public ResponseEntity<?> singleTopic(@PathVariable(value = "key") String key) {
		Optional<Topic> topic = topicService.findByKey(key);
		return ResponseEntity.status(HttpStatus.OK).body(topic);
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> addTopic(@RequestParam(value = "uploadFile", required = false) Optional<MultipartFile> file, @Valid @RequestParam("info") String info)
	throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		TopicRequest topRequest = objectMapper.readValue(info, TopicRequest.class);
		
		Topic newTopic = topicService.create(topRequest, file);
		if (newTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newTopic);
	}

	@PutMapping("/update/{key}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> updateTopic(@PathVariable(value = "key") String key, @RequestParam(value = "uploadFile", required = false) Optional<MultipartFile> file,
	@Valid @RequestParam("info") String info) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		TopicRequest topRequest = objectMapper.readValue(info, TopicRequest.class);
		
		Topic updateTopic = topicService.update(key, topRequest, file);
		if (updateTopic == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Subject or Topic not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateTopic);
	}

	@DeleteMapping("/delete/{key}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> deleteTopic(@PathVariable(value = "key") String key) {
		Optional<Topic> currTopic = topicService.findByKey(key);
		if (!currTopic.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found or already deleted."));
		}
		topicService.delete(currTopic.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Topic is successfully deleted"));
	}

	@GetMapping("/{key}/comments")
	public ResponseEntity<?> listCommentsTopic(@PathVariable(value = "key") String key) {
		Collection<Comment> comments = commentService.findByTopic(key);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}

	@GetMapping("/{key}/comments/{id}")
	public ResponseEntity<?> listCommentsParent(@PathVariable(value = "key") String key, @PathVariable(value = "id") String id) {
		Optional<Topic> currTopic = topicService.findByKey(key);
		if (!currTopic.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found or deleted."));
		}
		Collection<Comment> comments = commentService.findByComment(id);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
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

	@PutMapping("/{key}/like/add/{action}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> addLike(@PathVariable(value = "key") String key,  @PathVariable(value = "action") int action) {
		boolean liked = action == 1 ? true : false;
		Collection<Appreciation> appreciations = topicService.addAppreciation(key, liked);
		return ResponseEntity.status(HttpStatus.OK).body(appreciations);
	}

	@PutMapping("/{key}/like/delete/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('COACH')")
	public ResponseEntity<?> deleteLike(@PathVariable(value = "key") String key, @PathVariable(value = "id") int id) {
		boolean isDeleted = topicService.deleteAppreciation(key, id);
		if (isDeleted == false) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Topic not found or deleted."));
		}
		return ResponseEntity.status(HttpStatus.OK).body(isDeleted);
	}

	@GetMapping("/comments/last")
	public ResponseEntity<?> lastComments() {
		Collection<Comment> comments = commentService.getLastComments();
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}

	@GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) throws Exception {
    Resource file = topicService.loadFileAsResource(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}