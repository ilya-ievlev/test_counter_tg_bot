package com.counter.test_counter.service;

import com.counter.test_counter.model.TestResult;
import com.counter.test_counter.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestResultService {

    private final TestResultRepository testResultRepository;

    public void saveNewTestResult(TestResult testResult){

    }
}
