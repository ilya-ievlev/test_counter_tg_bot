package com.counter.test_counter.service;

import com.counter.test_counter.model.Test;
import com.counter.test_counter.model.TestResult;
import com.counter.test_counter.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Test saveNew(Test test) { // TODO: 13.11.2024 or should I return dto here
        if (test == null) {
            throw new IllegalArgumentException("test to save can't be null"); // TODO: 13.11.2024 or is it better to use custom exception or annotation here?
        }
        return testRepository.save(test);
    }

    public void delete(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("id can't be less, than 1");
        }
        testRepository.deleteById(id);
    }

    public void update(Test test) {
        Long testId = test.getId();
        if (testId == null || testId < 1) {
            throw new IllegalArgumentException("test id to update isn't valid");
        } // TODO: 13.11.2024 should I check if entity with required id available in db? 
        testRepository.save(test);
    }

    public Optional<Test> findById(long id) { // TODO: 13.11.2024 or should I return dto here
        return testRepository.findById(id);
    }


    public void saveTestResult(TestResult testResult) {

    }

    public void sighTestToTestResult(Test test) {
        // todo ask user if this is the test that he really want to asign or he wants to do something different or add this image as a result to other test
    }


}
