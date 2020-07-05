package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.payload.request.CommentRequest;

public interface CommentService {

  public Comment create(CommentRequest commRequest);

  public Comment update(String id, CommentRequest commRequest);

  public void delete(String id);

  public Optional<Comment> findById(String id);

  public Collection<Comment> findByTopic(String key);

  public Collection<Comment> findByHsitoire(String id);

  public Collection<Comment> findByComment(String id);

  public Collection<Comment> getLastComments();
}