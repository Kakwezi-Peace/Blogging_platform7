package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.model.Post;
import com.example.Blogging_platform2.model.PostView;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.PostViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostViewGraphQLController {

    private final PostViewService service;

    @QueryMapping
    public List<PostView> getViewsByPost(@Argument Long postId) {
        return service.getViewsByPost(postId);
    }

    @QueryMapping
    public PostView getView(@Argument Long id) {
        return service.getView(id);
    }

    @MutationMapping
    public PostView createView(@Argument Long postId, @Argument Long userId) {
        PostView view = new PostView();

        Post post = new Post();
        post.setId(postId);
        view.setPost(post);

        User user = new User();
        user.setId(userId);
        view.setUser(user);

        return service.createView(view);
    }

    @MutationMapping
    public Boolean deleteView(@Argument Long id) {
        return service.deleteView(id);
    }
}
