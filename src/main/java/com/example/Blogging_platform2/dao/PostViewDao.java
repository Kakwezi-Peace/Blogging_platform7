package com.example.Blogging_platform2.dao;

import com.example.Blogging_platform2.model.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostViewDao extends JpaRepository<PostView, Long> {

    List<PostView> findAllByPostId(Long postId);


}
