package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.payload.request.CommentRequest;

import org.springframework.data.domain.Page;

public interface CommentService {

  public Comment create(CommentRequest commRequest);

  public Comment update(String id, CommentRequest commRequest);

  public void delete(String id);

  public Optional<Comment> findById(String id);

  public Page<Comment> findByTopic(String key,int page, int size);

  public Collection<Comment> findByComment(String id);
}