package com.anonymasn.forum.dao;

import java.util.Collection;
import java.util.Optional;

import com.anonymasn.forum.model.Role;
import com.anonymasn.forum.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Collection<User> findByRolesIn(Collection<Role> roles);
}