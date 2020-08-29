package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.anonymasn.forum.dao.TopicDao;
import com.anonymasn.forum.dao.SubjectDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.TopicRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    if (!subject.get().getTypeSubject().isPublicType() && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      throw new RuntimeException("Access denied to create a topic in a subject with type is not public.");
    }

    String imgDefault = null;

    if(file.isPresent()) {
      imgDefault = uploadFile(file.get());
    }

    final Topic topic = new Topic(
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
    topicDao.save(topic);
    return topic;
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
  public Page<Topic> findBySubjectAndStatus(final String key, final int status, final int page, final int size) {
    final Optional<Subject> subject = subjectDao.findByKey(key);
    if (!subject.isPresent()) {
			return null;
    }
    int customSize = size;
    if (size == -1) {
      customSize = Integer.MAX_VALUE;
    }
    final Pageable pageable = PageRequest.of(page, customSize, Sort.by(Order.desc("id")));
    return topicDao.findBySubjectAndStatus(subject.get(), status, pageable);
  }

  @Override
  public Optional<Topic> findByKey(final String key) {
    return topicDao.findByKey(key);
  }

  @Override
  public Topic update(final String key, final TopicRequest subRequest, Optional<MultipartFile> file) {
    final Optional<Topic> currTopic = topicDao.findByKey(key);
    final Optional<Subject> subject = subjectDao.findById(subRequest.getSubject());
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!currTopic.isPresent() || !subject.isPresent()) {
			return null;
		}

    boolean authorized = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    String idCreateUser = currTopic.get().getCreateUser().getId();

    if ((!subject.get().getTypeSubject().isPublicType() && !authorized) || (!userDetails.getId().equals(idCreateUser) && !authorized)) {
      throw new RuntimeException("Access denied to update a topic in a subject with type is not public or you are not the author.");
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
    final Optional<Topic> currTopic = topicDao.findById(id);
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!currTopic.isPresent()) {
      return;
    }
    
    boolean authorized = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

    String idCreateUser = currTopic.get().getCreateUser().getId();

    if ((!currTopic.get().getSubject().getTypeSubject().isPublicType() && !authorized) || (!userDetails.getId().equals(idCreateUser) && !authorized)) {
      throw new RuntimeException("Access denied to delete a topic in a subject with type is not public you are not the author.");
    }

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
    // Create to upload file if not exist
    final Path fileStorageLocation = Paths.get("src/main/resources/public/images").toAbsolutePath().normalize();
    try {
      Files.createDirectories(fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
    }

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = "";

		try {
      if(originalFileName.contains("..")) {
        throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
      }

      String fileExtension = "";
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch(Exception e) {
        throw new RuntimeException("Sorry! Filename is invalid. " + originalFileName);
      }

      fileName = UUID.randomUUID().toString() + "-teaser" + fileExtension;
      Path targetLocation = fileStorageLocation.resolve(fileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
    }

    return fileName;
  }

  @Override
  public Resource loadFileAsResource(String fileName) throws Exception {
    final Path fileStorageLocation = Paths.get("src/main/resources/public/images").toAbsolutePath().normalize();

    try {
      Path filePath = fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName);
    }
  }

  @Override
  public Collection<Appreciation> addAppreciation(String key, boolean like) {
    final Optional<Topic> currTopic = topicDao.findByKey(key);
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> createUser = userDao.findById(userDetails.getId());

    if (!currTopic.isPresent() || !createUser.isPresent()) {
      return null;
    }

    Topic topic = currTopic.get();

    Set<Appreciation> appreciations = topic.getAppreciations();
    int numero = appreciations.size() == 0 ? 1 : appreciations.size();
    Appreciation appreciation = null;

    for (Appreciation appr : appreciations) {
      if (appr.getUser().getEmail().equals(createUser.get().getEmail())) {
        appreciation = appr;
        break;
      }
    }

    if (appreciation == null) {
      appreciation = new Appreciation(numero, createUser.get(), like);
      appreciations.add(appreciation);
    } else {
      Appreciation appr = appreciation;
      appreciations.remove(appr);
      appreciation.setLiked(like);
      appreciations.add(appreciation);
    }

    topic.setAppreciations(appreciations);
    
    topicDao.save(topic);

    return topic.getAppreciations();
  }

  @Override
  public boolean deleteAppreciation(String key, int numero) {
    final Optional<Topic> currTopic = topicDao.findByKey(key);
    
    if (!currTopic.isPresent()) {
      return false;
    }
    
    Topic topic = currTopic.get();

    Set<Appreciation> appreciations = topic.getAppreciations();

    for (Appreciation appr : appreciations) {
      if (appr.getNumero() == numero) {
        appreciations.remove(appr);
        break;
      }
    }

    topic.setAppreciations(appreciations);
    
    topicDao.save(topic);

    return true;
  }
}