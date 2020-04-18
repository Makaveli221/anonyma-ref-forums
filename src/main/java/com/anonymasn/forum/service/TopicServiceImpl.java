package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class TopicServiceImpl implements TopicService {

  @Autowired
  TopicDao topicDao;

  @Autowired
  SubjectDao subjectDao;

  @Autowired
  UserDao userDao;

  @Override
  public Topic create(final TopicRequest subRequest, Optional<MultipartFile> file) {
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final Optional<User> createUser = userDao.findById(userDetails.getId());

    final Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());

    if (!subject.isPresent()) {
			return null;
    }

    String imgDefault = null;

    if(file.isPresent()) {
      imgDefault = uploadFile(file.get());
    }

    final Topic Topic = new Topic(
      subRequest.getTitle(),
      subRequest.getDescription(),
      subRequest.getKeywords(),
      generateKey(subRequest.getTitle()),
      subRequest.getData(),
      imgDefault,
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
    int customSize = size;
    if (size == -1) {
      customSize = Integer.MAX_VALUE;
    }
    final Pageable pageable = PageRequest.of(page, customSize, Sort.by(Order.desc("id")));
    return topicDao.findBySubject(subject.get(), pageable);
  }

  @Override
  public Page<Topic> findByCreateUser(final String id,final int page, final int size) {
    final Optional<User> createUser = userDao.findById(id);
    if (!createUser.isPresent()) {
			return null;
    }
    final Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("id")));
    return topicDao.findByCreateUser(createUser.get(), pageable);
  }

  @Override
  public Optional<Topic> findByKey(final String key) {
    return topicDao.findByKey(key);
  }

  @Override
  public Topic update(final String key, final TopicRequest subRequest, Optional<MultipartFile> file) {
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
    topic.setData(subRequest.getData());
    topic.setStatus(subRequest.getStatus());
    if(file.isPresent()) {
      topic.setImgDefault(uploadFile(file.get()));
    }
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

  @Override
  public String uploadFile(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get("src/main/resources/public/images/" + fileName);

		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/images/")
			.path(fileName)
      .toUriString();
    return fileDownloadUri;
  }
}