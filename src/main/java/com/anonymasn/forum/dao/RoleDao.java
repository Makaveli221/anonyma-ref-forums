package com.anonymasn.forum.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anonymasn.forum.model.ERole;
import com.anonymasn.forum.model.Role;

@Repository
public interface RoleDao extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}