package com.counter.test_counter.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "name_substitute")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameSubstitute {

    @Id
    @Column(name = "id")
    @Min(1)
    @Unique
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;
}
