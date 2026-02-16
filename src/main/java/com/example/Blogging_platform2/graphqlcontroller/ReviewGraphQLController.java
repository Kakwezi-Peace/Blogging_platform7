package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.model.Post;
import com.example.Blogging_platform2.model.Review;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewGraphQLController {

    private final ReviewService service;

    @QueryMapping
    public List<Review> getReviewsByPost(@Argument Long postId) {
        return service.getReviewsByPost(postId);
    }

    @QueryMapping
    public Review getReview(@Argument Long reviewId) {
        return service.getReviewById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review with ID " + reviewId + " not found"));
    }

    @MutationMapping
    public Review createReview(@Argument Long postId,
                               @Argument Long userId,
                               @Argument int rating,
                               @Argument String comment) {
        Review review = new Review();

        Post post = new Post();
        post.setId(postId);
        review.setPost(post);

        User user = new User();
        user.setId(userId);
        review.setUser(user);

        review.setRating(rating);
        review.setComment(comment);

        return service.saveReview(review);
    }



    @MutationMapping
    public Boolean deleteReview(@Argument Long reviewId) {
        service.deleteReview(reviewId);
        return true;
    }
}
