package com.anonymasn.forum.dao;

import java.util.Optional;

import com.anonymasn.forum.model.Subject;
import com.anonymasn.forum.model.Typesubject;
import com.anonymasn.forum.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDao extends PagingAndSortingRepository<Subject, String> {
    public Optional<Subject> findByKey(String key);

    public Page<Subject> findByTypeSubject(Typesubject typeSubject, Pageable pageable);

    public Optional<Subject> findByTypeSubjectAndTitle(Typesubject typeSubject, String title);

    public Optional<Subject> findByTypeSubjectAndTitleAndCreateUser(Typesubject typeSubject, String title, User user);

    public Boolean existsByKey(String key);
}