package com.anonymasn.forum.dao;

import java.util.Collection;

import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.model.EMessage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends MongoRepository<Message, String> {  
	public Collection<Message> findByType(EMessage type);

	public Collection<Message> findByTypeAndEmail(EMessage type, String email);

	public Collection<Message> findTop3ByOrderByCreateDateDesc();

	public Collection<Message> findByValidateTrueAndPublishedTrue();

	public Collection<Message> findByPublishedTrue();
}