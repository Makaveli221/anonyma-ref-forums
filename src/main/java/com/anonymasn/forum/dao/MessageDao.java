package com.anonymasn.forum.dao;

import java.util.Collection;

import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.model.EMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends PagingAndSortingRepository<Message, String> {  
	public Page<Message> findByType(EMessage type, Pageable pageable);

	public Collection<Message> findByTypeAndEmail(EMessage type, String email);

	public Collection<Message> findTop3ByValidateAndPublishedOrderByCreateDateDesc(boolean validate, boolean published);

	public Collection<Message> findByValidateTrueAndPublishedTrue();

	public Page<Message> findByPublishedTrue(Pageable pageable);
}