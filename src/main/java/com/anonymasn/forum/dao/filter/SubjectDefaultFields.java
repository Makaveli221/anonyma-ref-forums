package com.anonymasn.forum.dao.filter;

import java.util.Set;

public interface SubjectDefaultFields {
  String getKey();
  String getTitle();
  Set<String> getKeywords();
}