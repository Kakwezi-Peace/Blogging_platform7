package com.example.Blogging_platform2.service;
import com.example.Blogging_platform2.dao.UserDao;
import com.example.Blogging_platform2.model.Role;
import com.example.Blogging_platform2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from DB
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert roles into GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getName) // e.g. ROLE_ADMIN
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // Return Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
