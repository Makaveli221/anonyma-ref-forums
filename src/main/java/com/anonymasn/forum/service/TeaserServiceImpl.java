package com.anonymasn.forum.service;

import java.util.Optional;
import java.util.Collection;
import java.util.UUID;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.anonymasn.forum.dao.TeaserDao;
import com.anonymasn.forum.dao.TypesubjectDao;
import com.anonymasn.forum.dao.UserDao;
import com.anonymasn.forum.model.Teaser;
import com.anonymasn.forum.model.Typesubject;
import com.anonymasn.forum.model.User;
import com.anonymasn.forum.payload.request.TeaserRequest;
import com.anonymasn.forum.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Service
public class TeaserServiceImpl implements TeaserService {
  
  @Autowired
  TeaserDao teaserDao;

  @Autowired
  TypesubjectDao typeSubjectDao;

  @Autowired
  UserDao userDao;

  public Teaser create(TeaserRequest teaserRequest, Optional<MultipartFile> file) {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> createUser = userDao.findById(userDetails.getId());

    Optional<Typesubject> typeSub = typeSubjectDao.findById(teaserRequest.getTypeSubject());

    if (!typeSub.isPresent()) {
			return null;
    }

    if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      throw new RuntimeException("Access denied to create a teaser.");
    }

    String presentation = null;

    if(file.isPresent()) {
      presentation = uploadFile(file.get());
    }

    Teaser teaser = new Teaser(
      teaserRequest.getTitle(),
      teaserRequest.getDescription(),
      teaserRequest.getKeywords(),
      typeSub.get(),
      presentation,
      createUser.get()
    );
    teaserDao.save(teaser);
    return teaser;
  }

  public Collection<Teaser> getAll() {
    return teaserDao.findAll();
  }

  @Override
  public Optional<Teaser> findById(final String id) {
    return teaserDao.findById(id);
  }

  @Override
  public Teaser update(String id, TeaserRequest teaserRequest, Optional<MultipartFile> file) {
    final Optional<Teaser> currTeaser = teaserDao.findById(id);
    final Optional<Typesubject> typeSub = typeSubjectDao.findById(teaserRequest.getTypeSubject());
    final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!currTeaser.isPresent() || !typeSub.isPresent()) {
			return null;
		}

    if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      throw new RuntimeException("Access denied to update a teaser.");
    }

    final Teaser teaser = currTeaser.get();
    teaser.setTitle(teaserRequest.getTitle());
    teaser.setDescription(teaserRequest.getDescription());
    teaser.setKeywords(teaserRequest.getKeywords());
    teaser.setTypeSubject(typeSub.get());
    if(file.isPresent()) {
      teaser.setPresentation(uploadFile(file.get()));
    }
    teaserDao.save(teaser);
    return teaser;
  }

  @Override
  public void delete(String id) {
    teaserDao.deleteById(id);
  }

  @Override
  public String uploadFile(MultipartFile file) {
    // Create to upload file if not exist
    final Path fileStorageLocation = Paths.get("src/main/resources/public/images").toAbsolutePath().normalize();
    try {
      Files.createDirectories(fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
    }

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = "";

		try {
      if(originalFileName.contains("..")) {
        throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
      }

      String fileExtension = "";
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch(Exception e) {
        throw new RuntimeException("Sorry! Filename is invalid. " + originalFileName);
      }

      fileName = UUID.randomUUID().toString() + "-teaser" + fileExtension;
      Path targetLocation = fileStorageLocation.resolve(fileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
    return fileName;
  }

  @Override
  public Resource loadFileAsResource(String fileName) throws Exception {
    final Path fileStorageLocation = Paths.get("src/main/resources/public/images").toAbsolutePath().normalize();

    try {
      Path filePath = fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName);
    }
  }
}