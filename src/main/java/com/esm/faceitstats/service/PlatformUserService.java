package com.esm.faceitstats.service;

import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

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

         var tempUser = this.repository.findById(id).get();
         tempUser.setUsername(user.getUsername());
         tempUser.setRole(user.getRole());
         tempUser.setFaceitLink(user.getFaceitLink());

        this.repository.save(tempUser);
    }

    public User getUser(Long id) {
         var user = this.repository.findById(id);
            if(user.isEmpty()) {
                throw new ResourceNotFoundException("User was not found");
            }

            return user.get();
    }

    public List<User> getUsers(String query){
        if(query != null){
            return this.repository.findByUsernameContaining(query);
        }
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
}
