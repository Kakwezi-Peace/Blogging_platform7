package com.example.Blogging_platform2.dao;

import com.example.Blogging_platform2.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
