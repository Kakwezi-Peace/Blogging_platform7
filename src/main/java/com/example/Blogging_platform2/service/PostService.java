package com.example.Blogging_platform2.service;

import com.example.Blogging_platform2.dao.PostDao;
import com.example.Blogging_platform2.exception.PostNotFoundException;
import com.example.Blogging_platform2.model.Post;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostDao postDao;

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    @Transactional
    @CachePut(value = "posts", key = "#result.id")
    public Post savePost(Post post) {
        return postDao.save(post);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#id")
    public Post getPostById(Long id) {
        return postDao.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postsByUser", key = "#userId")
    public Page<Post> getPostsByUser(Long userId, Pageable pageable) {
        return postDao.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#keyword")
    public Page<Post> searchPostsByTitle(String keyword, Pageable pageable) {
        return postDao.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(Long id) {
        if (!postDao.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "{#page, #size, #sortBy}")
    public Page<Post> getAllPosts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return postDao.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "'all'")
    public List<Post> getAllPosts() {
        return postDao.findAll();
    }

    @Transactional
    @CachePut(value = "posts", key = "#id")
    public Post updatePost(Long id, Post updatedPost) {
        Post existingPost = getPostById(id);
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        return postDao.save(existingPost);
    }
}
