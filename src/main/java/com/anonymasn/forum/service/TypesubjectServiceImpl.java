package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.dao.TypesubjectDao;
import com.anonymasn.forum.model.Typesubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypesubjectServiceImpl implements TypesubjectService {

  @Autowired
  TypesubjectDao typeSubjectDao;

  @Override
  public Typesubject create(Typesubject typeSubject) {
    typeSubjectDao.save(typeSubject);
        return typeSubject;
  }

  @Override
  public Collection<Typesubject> getAll() {
    return typeSubjectDao.findAll();
  }

  @Override
  public Optional<Typesubject> findById(String id) {
    return typeSubjectDao.findById(id);
  }

  @Override
  public Optional<Typesubject> findByName(String name) {
    return typeSubjectDao.findByName(name);
  }

  @Override
  public Typesubject update(Typesubject typeSubject) {
    typeSubjectDao.save(typeSubject);
    return typeSubject;
  }

  @Override
  public Boolean existsByName (String name) {
    return typeSubjectDao.existsByName(name);
  }

  @Override
  public void delete(String id) {
    typeSubjectDao.deleteById(id);
  }
}