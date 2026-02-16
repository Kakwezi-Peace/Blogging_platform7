package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.model.Tag;
import com.example.Blogging_platform2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TagGraphQLController {

    private final TagService service;

    @QueryMapping
    public List<Tag> getAllTags() {
        return service.getAllTags();
    }

    @QueryMapping
    public Optional<Tag> getTag(@Argument Long tagId) {
        return service.getTagById(tagId);
    }

    @MutationMapping
    public Tag createTag(@Argument String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return service.saveTag(tag);
    }

    @MutationMapping
    public Boolean deleteTag(@Argument Long tagId) {
        service.deleteTag(tagId);
        return true;
    }
}
