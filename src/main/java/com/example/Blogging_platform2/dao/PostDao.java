package com.example.Blogging_platform2.dao;

import com.example.Blogging_platform2.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
