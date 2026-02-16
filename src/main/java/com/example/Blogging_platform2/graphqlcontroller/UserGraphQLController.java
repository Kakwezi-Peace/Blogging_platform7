package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.model.Role;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.UserService;
import com.example.Blogging_platform2.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class UserGraphQLController {

    private final UserService service;

    @QueryMapping
    public List<User> getAllUsers() {
        return service.findAll();
    }

    @QueryMapping
    public User getUser(@Argument Long id) {
        return service.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    @MutationMapping
    public User createUser(@Argument String username,
                           @Argument String email,
                           @Argument String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Assign default role
        Role defaultRole = new Role();
        defaultRole.setName("ROLE_USER");
        user.setRoles(Set.of(defaultRole));

        return service.registerUser(user);
    }
}
