package com.counter.test_counter.service;

import com.counter.test_counter.model.User;
import com.counter.test_counter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUserExists(long id) {
        return userRepository.existsById(id);
    }


    public User saveNewUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user to save can't be null");
        } // todo what if user with such id already exists?
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
