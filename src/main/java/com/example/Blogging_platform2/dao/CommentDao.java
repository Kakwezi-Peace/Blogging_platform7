package com.example.Blogging_platform2.dao;

import com.example.Blogging_platform2.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
}

