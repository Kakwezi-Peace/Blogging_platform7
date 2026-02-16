package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.dto.CreatePostRequest;
import com.example.Blogging_platform2.model.Post;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.PostService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostGraphQLController {

    private final PostService postService;

    public PostGraphQLController(PostService postService) {
        this.postService = postService;
    }

    @QueryMapping
    public List<Post> allPosts() {
        return postService.getAllPosts();
    }

    @QueryMapping
    public Post postById(@Argument Long id) {
        return postService.getPostById(id); // returns Post directly
    }

    @MutationMapping
    public Post createPost(@Argument CreatePostRequest input) {
        Post post = new Post();
        post.setTitle(input.getTitle());
        post.setContent(input.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());


        User user = new User();
        user.setId(input.getUserId());
        post.setUser(user);

        return postService.savePost(post);
    }

    @MutationMapping
    public Post updatePost(@Argument Long id, @Argument CreatePostRequest input) {
        Post updatedPost = new Post();
        updatedPost.setTitle(input.getTitle());
        updatedPost.setContent(input.getContent());
        return postService.updatePost(id, updatedPost);
    }

    @MutationMapping
    public Boolean deletePost(@Argument Long id) {
        postService.deletePost(id);
        return true;
    }
}
