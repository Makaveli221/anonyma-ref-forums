package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.dao.CommentDao;
import com.anonymasn.forum.dao.TopicDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.CommentRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  CommentDao commentDao;

  @Autowired
  TopicDao topicDao;

  @Autowired
  UserDao userDao;

  @Override
  public Comment create(CommentRequest commRequest) {
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final Optional<User> createUser = userDao.findById(userDetails.getId());

    Comment comment = new Comment(commRequest.getMessage(), createUser.get());

    switch (commRequest.getSource()) {
      case "topic":
        Optional<Topic> topic = topicDao.findById(commRequest.getIdSource());
        if(!topic.isPresent()) {
          return null;
        }
        comment.setTopic(topic.get());
        break;
      
      case "comment":
        Optional<Comment> parent = commentDao.findById(commRequest.getIdSource());
        if(!parent.isPresent()) {
          return null;
        }
        comment.setParent(parent.get());
        break;
    
      default:
        break;
    }
    
    commentDao.save(comment);
    return comment;
  }

  @Override
  public Comment update(String id, CommentRequest commRequest) {
    Optional<Comment> currComment = commentDao.findById(id);
    if (!currComment.isPresent()) {
      return  null;
    }
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean authorized = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

    String idCreateUser = currComment.get().getCreateUser().getId();

    if (!userDetails.getId().equals(idCreateUser) && !authorized) {
      throw new RuntimeException("Access denied to update a comment that are not the author.");
    }

    Comment comment = currComment.get();
    comment.setMessage(commRequest.getMessage());
    commentDao.save(comment);
    return comment;
  }

  @Override
  public void delete(String id) {
    Optional<Comment> currComment = commentDao.findById(id);
    if (!currComment.isPresent()) {
      return;
    }
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean authorized = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

    String idCreateUser = currComment.get().getCreateUser().getId();

    if (!userDetails.getId().equals(idCreateUser) && !authorized) {
      throw new RuntimeException("Access denied to delete a comment that are not the author.");
    }
    commentDao.deleteById(id);
  }

  @Override
  public Page<Comment> findByTopic(String key,int page, int size) {
    final Optional<Topic> topic = topicDao.findByKey(key);
    if (!topic.isPresent()) {
			return null;
    }
    final Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("createDate")));
    return commentDao.findByTopic(topic.get(), pageable);
  }

  @Override
  public Collection<Comment> findByComment(String id) {
    final Optional<Comment> parent = commentDao.findById(id);
    if (!parent.isPresent()) {
			return null;
    }
    return commentDao.findByParent(parent.get());
  }

}