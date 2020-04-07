package com.anonymasn.forum.dao;

import java.util.Collection;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.model.Subject;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends MongoRepository<Topic, String> {
    public Collection<Topic> findBySubject(Subject subject);
    
    public Collection<Topic> findBySubjectAndTitle(Subject subject, String title);

    public Collection<Topic> findBySubjectAndTitleAndCreateUser(Subject subject, String title, User user);

    public Boolean existsByKey(String key);
}