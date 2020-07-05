package com.anonymasn.forum.dao;

import java.util.Optional;

import com.anonymasn.forum.model.Teaser;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeaserDao extends MongoRepository<Teaser, String> {  
	public Optional<Teaser> findByTitle(String title);

	public Boolean existsByTitle(String title);
}