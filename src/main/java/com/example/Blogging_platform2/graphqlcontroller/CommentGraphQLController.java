package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.dto.CommentDto;
import com.example.Blogging_platform2.model.Comment;
import com.example.Blogging_platform2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentGraphQLController {

    private final CommentService service;

    @QueryMapping
    public List<Comment> getCommentsByPost(@Argument Long postId) {
        return service.getCommentsByPost(postId);
    }

    @QueryMapping
    public Comment getComment(@Argument Long commentId) {
        return service.getCommentById(commentId);
    }

    @MutationMapping
    public Comment createComment(@Argument CommentDto dto) {
        return service.saveComment(dto);
    }

    @MutationMapping
    public Boolean deleteComment(@Argument Long commentId) {
        service.deleteComment(commentId);
        return true;
    }
}
