package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.payload.request.MessageRequest;

public interface MessageService {

  public Message create(MessageRequest messRequest);

  public Message update(String id, MessageRequest messRequest);

  public void delete(String id);

  public Optional<Message> findById(String id);

  public Collection<Message> findByPublished();

  public Collection<Message> findByTypeAndEmail(String typeRequest, String email);

  public Collection<Message> findByType(String typeRequest);

  public Collection<Appreciation> addAppreciation(String key, boolean like);

  public boolean deleteAppreciation(String key, int numero);

  public Collection<Message> getLastMessages();
}