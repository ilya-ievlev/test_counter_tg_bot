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
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @Min(1)
    @Unique
    @NotNull
    private Long id;

    @Column(name = "first_name")
    @NotNull
    @NotBlank
    private String firstName;


    @Column(name = "last_name")
    @NotBlank
    @NotNull
    private String lastName;


    @Column(name = "username")
    @NotBlank
    @NotNull
    @Unique
    @Size() // TODO: 07.11.2024 use real tg constraints here
    private String username;

    @Column(name = "is_bot_admin")
    private boolean isBotAdmin;

    @ManyToMany
    @JoinTable(
            name = "users_tests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Test> testList;

    public User(
//            Long chatId,
            String firstName, String lastName, String username) {
//        this.chatId = chatId; // TODO: 07.11.2024 check if we need chatId in group settings, or do we need something else here
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
