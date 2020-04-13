package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Random;

import com.anonymasn.forum.dao.TopicDao;
import com.anonymasn.forum.dao.SubjectDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.TopicRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

  @Autowired
  TopicDao topicDao;

  @Autowired
  SubjectDao subjectDao;

  @Autowired
  UserDao userDao;

  @Override
  public Topic create(final TopicRequest subRequest) {
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final Optional<User> createUser = userDao.findById(userDetails.getId());

    final Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());

    if (!subject.isPresent()) {
			return null;
    }
    
    // subRequest.getImgDefault();

    final Topic Topic = new Topic(
      subRequest.getTitle(),
      subRequest.getDescription(),
      subRequest.getKeywords(),
      generateKey(subRequest.getTitle()),
      subRequest.getData(),
      null,
      subject.get(),
      createUser.get(),
      subRequest.getStatus()
    );
    topicDao.save(Topic);
    return Topic;
  }

  @Override
  public Iterable<Topic> getAll() {
    return topicDao.findAll();
  }

  @Override
  public Page<Topic> findBySubject(final String key,final int page, final int size) {
    final Optional<Subject> subject = subjectDao.findByKey(key);
    if (!subject.isPresent()) {
			return null;
    }
    final Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("id")));
    return topicDao.findBySubject(subject.get(), pageable);
  }

  @Override
  public Optional<Topic> findByKey(final String key) {
    return topicDao.findByKey(key);
  }

  @Override
  public Topic update(final String key, final TopicRequest subRequest) {
    final Optional<Topic> currTopic = topicDao.findByKey(key);
    final Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());

    if (!currTopic.isPresent() || !subject.isPresent()) {
			return null;
		}

    final Topic topic = currTopic.get();
    topic.setTitle(subRequest.getTitle());
    topic.setDescription(subRequest.getDescription());
    topic.setKeywords(subRequest.getKeywords());
    topic.setSubject(subject.get());
    topic.setStatus(subRequest.getStatus());
    topicDao.save(topic);
    return topic;
  }

  @Override
  public void delete(final String id) {
    topicDao.deleteById(id);
  }

  @Override
  public String generateKey(final String title) {
    String key = title.replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll("\\s", "-").toLowerCase();
    final Random rand = new Random();
    while (topicDao.existsByKey(key)) {
      key += rand.nextInt(100000);
    }
    return key;
  }
}