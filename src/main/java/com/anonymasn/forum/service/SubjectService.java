package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.dao.filter.SubjectDefaultFields;
import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.payload.request.SubjectRequest;

import org.springframework.data.domain.Page;

public interface SubjectService {

  public Subject create(SubjectRequest subRequest);

  public Page<Subject> getAll(final int page, final int size);

  public Collection<SubjectDefaultFields> findByTypeSubject(final String name);

  public Page<Subject> findByTypeSubject(final String name, final int page, final int limit);

  public Collection<Subject> findByTypeSubjectWithPublicType(final boolean publicType);
  
  public Optional<Subject> findByKey(String key);

  public Subject update(String id, SubjectRequest subRequest);

  public void delete(String id);

  public String generateKey(String title);
}