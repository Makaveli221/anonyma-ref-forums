package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.List;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
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

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public Topic create(TopicRequest subRequest) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> createUser = userDao.findById(userDetails.getId());

    Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());

    if (!subject.isPresent()) {
			return null;
    }
    
    // subRequest.getImgDefault();

    Topic Topic = new Topic(
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
  public Collection<Topic> getAll() {
    return topicDao.findAll();
  }

  @Override
  public Page<Topic> findBySubject(String id,int page, int limit) {
    Optional<Subject> subject = subjectDao.findById(id);
    if (!subject.isPresent()) {
			return null;
    }

    final Pageable pageable = PageRequest.of(page, limit);
    Query query = new Query().with(pageable);
    query.addCriteria(Criteria.where("subject").is(subject.get()));

    List <Topic> filteredTopics = mongoTemplate.find(query, Topic.class);

    Page<Topic> topicPage = PageableExecutionUtils.getPage(
      filteredTopics,
      pageable,
      () -> mongoTemplate.count(query, Topic.class)
    );

    return topicPage;
  }

  @Override
  public Optional<Topic> findById(String id) {
    return topicDao.findById(id);
  }

  @Override
  public Topic update(String id, TopicRequest subRequest) {
    Optional<Topic> currTopic = topicDao.findById(id);
    Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());

    if (!currTopic.isPresent() || !subject.isPresent()) {
			return null;
		}

    Topic topic = currTopic.get();
    topic.setTitle(subRequest.getTitle());
    topic.setDescription(subRequest.getDescription());
    topic.setKeywords(subRequest.getKeywords());
    topic.setSubject(subject.get());
    topic.setStatus(subRequest.getStatus());
    topicDao.save(topic);
    return topic;
  }

  @Override
  public void delete(String id) {
    topicDao.deleteById(id);
  }

  @Override
  public String generateKey(String title) {
    String key = title.replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll("\\s", "-").toLowerCase();
    Random rand = new Random();
    while (topicDao.existsByKey(key)) {
      key += rand.nextInt(100000);
    }
    return key;
  }
}