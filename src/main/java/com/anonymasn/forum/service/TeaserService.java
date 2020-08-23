package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Collection;

import com.anonymasn.forum.model.Teaser;
import com.anonymasn.forum.payload.request.TeaserRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface TeaserService {

  public Teaser create(TeaserRequest teaserRequest, Optional<MultipartFile> file);

  public Collection<Teaser> getAll();

  public Optional<Teaser> findById(String id);

  public Teaser update(String id, TeaserRequest teaserRequest, Optional<MultipartFile> file);

  public void delete(String id);

  public String uploadFile(MultipartFile file);

  public Resource loadFileAsResource(String fileName) throws Exception;
}