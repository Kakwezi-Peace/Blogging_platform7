package com.example.Blogging_platform2.dao;

import com.example.Blogging_platform2.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDao extends JpaRepository<Review, Long> {

    List<Review> findAllByPostId(Long postId);

}
