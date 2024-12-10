package com.counter.test_counter.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weekly_report_results")
public class WeeklyReportResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "weekly_report_id")
    private WeeklyReport weeklyReport;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_executor_id")
    private User executor;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_uploaded_by_id")
    private User uploadedBy;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "file_id")
    private String fileId; // todo some fields can be null in order to  be able to save results without photos
}
