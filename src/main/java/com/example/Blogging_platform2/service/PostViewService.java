package com.example.Blogging_platform2.service;
import com.example.Blogging_platform2.dao.PostViewDao;
import com.example.Blogging_platform2.exception.PostViewNotFoundException;
import com.example.Blogging_platform2.model.PostView;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostViewService {

    private final PostViewDao postViewDao;

    public PostViewService(PostViewDao postViewDao) {
        this.postViewDao = postViewDao;
    }

    @Transactional
    @CachePut(value = "postViews", key = "#result.id")
    public PostView savePostView(PostView view) {
        return postViewDao.save(view);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postViews", key = "#postId")
    public List<PostView> getViewsByPost(Long postId) {
        return postViewDao.findAllByPostId(postId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postViews", key = "#id")
    public Optional<PostView> getPostViewById(Long id) {
        return postViewDao.findById(id);
    }

    @Transactional
    @CacheEvict(value = "postViews", key = "#id")
    public void deletePostView(Long id) {
        if (!postViewDao.existsById(id)) {
            throw new PostViewNotFoundException("View with ID " + id + " not found");
        }
        postViewDao.deleteById(id);
    }

    @Transactional
    @CachePut(value = "postViews", key = "#result.id")
    public PostView createView(PostView view) {
        return postViewDao.save(view);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postViews", key = "#id")
    public PostView getView(Long id) {
        return postViewDao.findById(id)
                .orElseThrow(() -> new PostViewNotFoundException("View with ID " + id + " not found"));
    }

    @Transactional
    @CacheEvict(value = "postViews", key = "#id")
    public Boolean deleteView(Long id) {
        if (!postViewDao.existsById(id)) {
            throw new PostViewNotFoundException("View with ID " + id + " not found");
        }
        postViewDao.deleteById(id);
        return true;
    }
}
