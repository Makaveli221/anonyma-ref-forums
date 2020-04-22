package com.anonymasn.forum.dao;

import java.util.Optional;

import com.anonymasn.forum.model.Typesubject;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesubjectDao extends MongoRepository<Typesubject, String> {  
	public Optional<Typesubject> findByName(String name);
	
	public Boolean existsByName(String name);
}