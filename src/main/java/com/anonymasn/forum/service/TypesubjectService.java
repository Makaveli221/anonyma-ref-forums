package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Typesubject;

public interface TypesubjectService {

  public Typesubject create(Typesubject typeSubject);

  public Collection<Typesubject> getAll();

  public Optional<Typesubject> findById(String id);

  public Typesubject update(Typesubject typeSubject);

  public Boolean existsByName (String name);

  public void delete(String id);
}