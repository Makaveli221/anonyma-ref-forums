package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.payload.request.MessageRequest;

import org.springframework.data.domain.Page;

public interface MessageService {

  public Message create(MessageRequest messRequest);

  public Message update(String id, MessageRequest messRequest);

  public void delete(String id);

  public Optional<Message> findById(String id);

  public Page<Message> findByPublished(int page, int size);

  public Collection<Message> findByTypeAndEmail(String typeRequest, String email);

  public Page<Message> findByType(String typeRequest, int page, int size);

  public Collection<Appreciation> addAppreciation(String key, boolean like);

  public boolean deleteAppreciation(String key, int numero);

  public Collection<Message> getLastMessages();
}