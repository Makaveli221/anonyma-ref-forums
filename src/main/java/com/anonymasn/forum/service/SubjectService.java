package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.payload.request.SubjectRequest;

public interface SubjectService {

  public Subject create(SubjectRequest subRequest);

  public Collection<Subject> getAll();

  public Optional<Subject> findById(String id);

  public Subject update(String id, SubjectRequest subRequest);

  public void delete(String id);

  public String generateKey(String title);
}