package com.anonymasn.forum.dao;

import com.anonymasn.forum.model.Typesubject;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesubjectDao extends MongoRepository<Typesubject, String> {  
	public Typesubject findByName(String name);
	
	public Boolean existsByName(String name);
}