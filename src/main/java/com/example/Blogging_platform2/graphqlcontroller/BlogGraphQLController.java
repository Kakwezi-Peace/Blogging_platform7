//package com.example.Blogging_platform2.graphqlcontroller;
//
//import com.example.Blogging_platform2.dto.CreatePostRequest;
//import com.example.Blogging_platform2.model.Post;
//import com.example.Blogging_platform2.service.PostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequiredArgsConstructor
//public class BlogGraphQLController {
//
//    private final PostService postService;
//
//    @QueryMapping
//    public List<Post> allPosts() {
//        return postService.getAllPosts();
//    }
//
//
//    @QueryMapping
//    public Optional<Post> postById(@Argument Long id) {
//        return Optional.ofNullable(postService.getPostById(id));
//    }
//
//    @MutationMapping
//    public Post createPost(@Argument CreatePostRequest input) {
//        Post post = new Post();
//        post.setTitle(input.getTitle());
//        post.setContent(input.getContent()); // fixed: use content, not title
//        return postService.savePost(post);
//    }
//}
