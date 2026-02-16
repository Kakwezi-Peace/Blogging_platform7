package com.example.Blogging_platform2.service;
import com.example.Blogging_platform2.model.Review;
import com.example.Blogging_platform2.dao.ReviewDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Transactional
    @CachePut(value = "reviews", key = "#result.id")
    public Review saveReview(Review review) {
        return reviewDao.save(review);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "reviews", key = "#id")
    public Optional<Review> getReviewById(Long id) {
        return reviewDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "reviews", key = "'all'")
    public List<Review> getAllReviews() {
        return reviewDao.findAll();
    }

    @Transactional
    @CacheEvict(value = "reviews", key = "#id")
    public void deleteReview(Long id) {
        reviewDao.deleteById(id);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "reviews", key = "#postId")
    public List<Review> getReviewsByPost(Long postId) {
        return reviewDao.findAllByPostId(postId);
    }
}
