package com.counter.test_counter.repository;

import com.counter.test_counter.model.WeeklyReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyReportResultRepository extends JpaRepository<WeeklyReportResult, Long> {
}
