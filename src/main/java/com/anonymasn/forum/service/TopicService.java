package com.anonymasn.forum.service;

import java.util.Optional;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface TopicService {

  public Topic create(TopicRequest subRequest, MultipartFile file);

  public Iterable<Topic> getAll();

  public Page<Topic> findBySubject(String id,int page, int limit);

  public Page<Topic> findByCreateUser(String id,int page, int limit);

  public Optional<Topic> findByKey(String id);

  public Topic update(String id, TopicRequest subRequest);

  public void delete(String id);

  public String generateKey(String title);

  public String uploadFile(MultipartFile file);
}