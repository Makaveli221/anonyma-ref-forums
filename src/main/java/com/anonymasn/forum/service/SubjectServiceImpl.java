package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Random;

import com.anonymasn.forum.dao.SubjectDao;
import com.anonymasn.forum.dao.TypesubjectDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Typesubject;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.SubjectRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;


@Service
public class SubjectServiceImpl implements SubjectService {

  @Autowired
  SubjectDao subjectDao;

  @Autowired
  TypesubjectDao typeSubjectDao;

  @Autowired
  UserDao userDao;

  @Override
  public Subject create(SubjectRequest subRequest) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> createUser = userDao.findById(userDetails.getId());

    Optional<Typesubject> typeSub = typeSubjectDao.findById(subRequest.getTypeSubject());

    if (!typeSub.isPresent()) {
			return null;
		}

    Subject subject = new Subject(
      subRequest.getTitle(),
      subRequest.getDescription(),
      subRequest.getKeywords(),
      typeSub.get(),
      createUser.get(),
      generateKey(subRequest.getTitle()),
      subRequest.getStatus()
    );
    subjectDao.save(subject);
    return subject;
  }

  @Override
  public Page<Subject> getAll(final int page, final int size) {
    if (size == -1) {
      return subjectDao.findAll(Pageable.unpaged());
    }
    return subjectDao.findAll(PageRequest.of(page, size, Sort.by(Order.desc("id"))));
  }

  @Override
  public Optional<Subject> findByKey(String key) {
    return subjectDao.findByKey(key);
  }

  @Override
  public Subject update(String key, SubjectRequest subRequest) {
    Optional<Subject> currSub = subjectDao.findByKey(key);
    Optional<Typesubject> typeSub = typeSubjectDao.findById(subRequest.getTypeSubject());

    if (!currSub.isPresent() || !typeSub.isPresent()) {
			return null;
		}

    Subject subject = currSub.get();
    subject.setTitle(subRequest.getTitle());
    subject.setDescription(subRequest.getDescription());
    subject.setKeywords(subRequest.getKeywords());
    subject.setTypeSubject(typeSub.get());
    subject.setStatus(subRequest.getStatus());
    subjectDao.save(subject);
    return subject;
  }

  @Override
  public void delete(String id) {
    subjectDao.deleteById(id);
  }

  @Override
  public String generateKey(String title) {
    String key = title.replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll("\\s", "-").toLowerCase();
    Random rand = new Random();
    while (subjectDao.existsByKey(key)) {
      key += rand.nextInt(100000);
    }
    return key;
  }
}