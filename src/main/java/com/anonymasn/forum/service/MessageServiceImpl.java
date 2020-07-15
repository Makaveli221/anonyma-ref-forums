package com.anonymasn.forum.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.anonymasn.forum.dao.MessageDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Appreciation;
import com.anonymasn.forum.model.EMessage;
import com.anonymasn.forum.model.Message;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.MessageRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService  {

  @Autowired
  MessageDao messageDao;

  @Autowired
  UserDao userDao;


  @Override
  public Message create(MessageRequest messRequest) {
    EMessage type = EMessage.CHATBOX;
    if(messRequest.getType().equals("history")) {
      type = EMessage.HISTORY;
    }

    Message message = new Message(type, messRequest.getEmail(), messRequest.getTexte());

    messageDao.save(message);
    return message;
  }

  @Override
  public Message update(String id, MessageRequest messRequest) {
    Optional<Message> currmessage = messageDao.findById(id);
    if (!currmessage.isPresent()) {
      return  null;
    }

    Message message = currmessage.get();
    message.setEmail(messRequest.getEmail());
    message.setTexte(messRequest.getTexte());
    message.setResponse(messRequest.getResponse());
    message.setValidate(messRequest.isValidate());
    message.setPublished(messRequest.isPublished());
    messageDao.save(message);
    return message;
  }

  @Override
  public void delete(String id) {
    Optional<Message> currMessage = messageDao.findById(id);
    if (!currMessage.isPresent()) {
      return;
    }

    messageDao.deleteById(id);
  }

  @Override
  public Optional<Message> findById(String id) {
    return messageDao.findById(id);
  }

  @Override
  public Collection<Message> findByPublished() {
    return messageDao.findByPublishedTrue();
  }

  @Override
  public Collection<Message> findByTypeAndEmail(String typeRequest, String email) {
    EMessage type = EMessage.HISTORY;
    if(typeRequest.equals("chatbox")) {
      type = EMessage.CHATBOX;
    }

    return messageDao.findByTypeAndEmail(type, email);
  }

  @Override
  public Collection<Message> findByType(String typeRequest) {
    EMessage type = EMessage.HISTORY;
    if(typeRequest.equals("chatbox")) {
      type = EMessage.CHATBOX;
    }

    return messageDao.findByType(type);
  }

  @Override
  public Collection<Appreciation> addAppreciation(String id, boolean like) {
    final Optional<Message> currMessage = messageDao.findById(id);
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> createUser = userDao.findById(userDetails.getId());

    if (!currMessage.isPresent() || !createUser.isPresent()) {
      return null;
    }

    Message message = currMessage.get();

    Set<Appreciation> appreciations = message.getAppreciations();
    int numero = appreciations.size() == 0 ? 1 : appreciations.size();
    Appreciation appreciation = null;

    for (Appreciation appr : appreciations) {
      if (appr.getUser().getEmail().equals(createUser.get().getEmail())) {
        appreciation = appr;
        break;
      }
    }

    if (appreciation == null) {
      appreciation = new Appreciation(numero, createUser.get(), like);
      appreciations.add(appreciation);
    } else {
      Appreciation appr = appreciation;
      appreciations.remove(appr);
      appreciation.setLiked(like);
      appreciations.add(appreciation);
    }

    message.setAppreciations(appreciations);
    
    messageDao.save(message);

    return message.getAppreciations();
  }

  @Override
  public boolean deleteAppreciation(String id, int numero) {
    final Optional<Message> currMessage = messageDao.findById(id);
    
    if (!currMessage.isPresent()) {
      return false;
    }
    
    Message message = currMessage.get();

    Set<Appreciation> appreciations = message.getAppreciations();

    for (Appreciation appr : appreciations) {
      if (appr.getNumero() == numero) {
        appreciations.remove(appr);
        break;
      }
    }

    message.setAppreciations(appreciations);
    
    messageDao.save(message);

    return true;
  }

  public Collection<Message> getLastMessages() {
    return messageDao.findTop3ByValidateAndPublishedOrderByCreateDateDesc(true, true);
  }
}