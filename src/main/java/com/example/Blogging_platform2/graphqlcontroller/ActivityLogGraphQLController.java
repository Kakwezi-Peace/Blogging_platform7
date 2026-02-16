package com.example.Blogging_platform2.graphqlcontroller;

import com.example.Blogging_platform2.model.ActivityLog;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ActivityLogGraphQLController {

    private final ActivityLogService service;

    @QueryMapping
    public List<ActivityLog> getAllLogs() {
        return service.getAllLogs();
    }

    @QueryMapping
    public ActivityLog getLog(@Argument Long id) {
        return service.getLog(id);
    }

    @MutationMapping
    public ActivityLog createLog(@Argument Long userId,
                                 @Argument String action,
                                 @Argument Long targetId,
                                 @Argument String details) {
        ActivityLog log = new ActivityLog();

        User user = new User();
        user.setId(userId);
        log.setUser(user);

        log.setAction(action);
        log.setTargetId(targetId);
        log.setDetails(details);

        return service.createLog(log);
    }

    @MutationMapping
    public Boolean deleteLog(@Argument Long id) {
        return service.deleteLogReturnBoolean(id);
    }
}
