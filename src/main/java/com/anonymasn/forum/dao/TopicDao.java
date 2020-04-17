package com.anonymasn.forum.dao;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Topic;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.model.Subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends PagingAndSortingRepository<Topic, String> {
    public Optional<Topic> findByKey(String key);

    public Page<Topic> findBySubject(Subject subject, Pageable pageable);

    public Page<Topic> findByCreateUser(User user, Pageable pageable);
    
    public Collection<Topic> findBySubjectAndTitle(Subject subject, String title);

    public Collection<Topic> findBySubjectAndTitleAndCreateUser(Subject subject, String title, User user);

    public Boolean existsByKey(String key);
}