package com.anonymasn.forum.dao;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends MongoRepository<Comment, String> {
    Optional<Comment> findByCreateUserAndTopic(User user, Topic topic);

    Optional<Comment> findByCreateUserAndParent(User user, Comment comment);

    public Page<Comment> findByTopic(Topic topic, Pageable pageable);

    public Collection<Comment> findByParent(Comment comment);

}