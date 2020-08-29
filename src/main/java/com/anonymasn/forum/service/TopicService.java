package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Collection;

import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface TopicService {

  public Topic create(TopicRequest subRequest, Optional<MultipartFile> file);

  public Iterable<Topic> getAll();

  public Page<Topic> findBySubject(String id, int page, int limit);

  public Page<Topic> findByCreateUser(String id, int page, int limit);

  public Page<Topic> findBySubjectAndStatus(final String key, final int status, final int page, final int size);

  public Optional<Topic> findByKey(String id);

  public Topic update(String id, TopicRequest subRequest, Optional<MultipartFile> file);

  public void delete(String id);

  public String generateKey(String title);

  public String uploadFile(MultipartFile file);

  public Resource loadFileAsResource(String fileName) throws Exception;

  public Collection<Appreciation> addAppreciation(String key, boolean like);

  public boolean deleteAppreciation(String key, int numero);
}