package com.anonymasn.forum.dao;

import java.util.Optional;

import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Typesubject;
import com.anonymasn.forum.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDao extends MongoRepository<Subject, String> {
    public Optional<Subject> findByTypeSubjectAndTitle(Typesubject typeSubject, String title);

    public Optional<Subject> findByTypeSubjectAndTitleAndCreateUser(Typesubject typeSubject, String title, User user);
}