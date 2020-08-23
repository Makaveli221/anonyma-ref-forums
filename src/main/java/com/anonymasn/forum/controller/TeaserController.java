package com.anonymasn.forum.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import com.anonymasn.forum.model.Teaser;
import com.anonymasn.forum.payload.request.TeaserRequest;
import com.anonymasn.forum.payload.response.MessageResponse;
import com.anonymasn.forum.service.TeaserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/teaser")
public class TeaserController {

  @Autowired
  TeaserService teaserService;
  
  @GetMapping("/all")
	public ResponseEntity<?> AllTeaser() {
    Collection<Teaser> teasers = teaserService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(teasers);
  }
  
  @PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addTeaser(@RequestParam(value = "uploadFile", required = false) Optional<MultipartFile> file, @Valid @RequestParam("info") String info)
	throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		TeaserRequest teaserRequest = objectMapper.readValue(info, TeaserRequest.class);
		
		Teaser newTeaser = teaserService.create(teaserRequest, file);
		if (newTeaser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Type subject not found"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newTeaser);
  }
  
  @PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateTeaser(@PathVariable(value = "id") String id, @RequestParam(value = "uploadFile", required = false) Optional<MultipartFile> file,
	@Valid @RequestParam("info") String info) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		TeaserRequest teaserRequest = objectMapper.readValue(info, TeaserRequest.class);
		
		Teaser updateTeaser = teaserService.update(id, teaserRequest, file);
		if (updateTeaser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Type subject or Teaser not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(updateTeaser);
  }
  
  @DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTeaser(@PathVariable(value = "id") String id) {
		Optional<Teaser> currTeaser = teaserService.findById(id);
		if (!currTeaser.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Teaser not found or already deleted."));
		}
		teaserService.delete(currTeaser.get().getId());
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Teaser is successfully deleted"));
	}

	// @RequestMapping("/file/{fileName}")
	// @ResponseBody
	// public HttpEntity<byte[]> getPhoto(@PathVariable String fileName) throws IOException {
	// 	final Path fileStorageLocation = Paths.get("src/main/resources/public/images").toAbsolutePath().normalize();
	// 	Path filePath = fileStorageLocation.resolve(fileName).normalize();

	// 	byte[] file = FileUtils.readFileToByteArray(new File(filePath.toUri()));

	// 	HttpHeaders headers = new HttpHeaders();
	// 	String contentType = "application/octet-stream";
	// 	headers.setContentType(MediaType.parseMediaType(contentType)); 
	// 	headers.setContentLength(file.length);
	// 	return new HttpEntity<byte[]>(file, headers);
	// }

	@GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) throws Exception {
    Resource file = teaserService.loadFileAsResource(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}