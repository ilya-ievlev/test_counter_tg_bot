package com.counter.test_counter.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weekly_reports")
public class WeeklyReport {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_of_the_week")
    @NotNull
    private Date startOfTheWeek;

    @Column(name = "end_of_the_week")
    @NotNull
    private Date endOfTheWeek;
}
