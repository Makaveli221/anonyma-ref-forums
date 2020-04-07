package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.payload.request.TopicRequest;

public interface TopicService {

  public Topic create(TopicRequest subRequest);

  public Collection<Topic> getAll();

  public Collection<Topic> findBySubject(String id);

  public Optional<Topic> findById(String id);

  public Topic update(String id, TopicRequest subRequest);

  public void delete(String id);

  public String generateKey(String title);
}