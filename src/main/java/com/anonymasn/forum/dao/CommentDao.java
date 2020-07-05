package com.anonymasn.forum.dao;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Comment;
import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends MongoRepository<Comment, String> {
    public Optional<Comment> findByCreateUserAndTopic(User user, Topic topic);

    public Optional<Comment> findByCreateUserAndParent(User user, Comment comment);

    public Collection<Comment> findByTopicOrderByCreateDateAsc(Topic topic);

    public Collection<Comment> findByHistoireOrderByCreateDateAsc(Message histoire);

    public Collection<Comment> findByParent(Comment comment);

    public Collection<Comment> findTop5ByOrderByCreateDateDesc();
}