package com.esm.faceitstats.service;

import com.esm.faceitstats.entity.Role;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlatformUserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

    }

    public void updateUser(Long id, User user) {
        if(!this.repository.existsById(id)){
            throw new ResourceNotFoundException("User was not found");
        }

        this.repository.save(user);
    }

    public List<User> getUsers(){
        return this.repository.findAll();
    }

    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("User was not found");
        }
        repository.deleteById(id);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public void setAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
