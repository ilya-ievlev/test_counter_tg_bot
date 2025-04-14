package com.counter.test_counter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_results")
public class TestResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @NotNull
    private Test test;

    @Column(name = "date_of_result")
    @NotNull
    private Date dateOfResult;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_executor_id")
    private User executor; //username or nickname of user who send the message, or tagged person if available. null if something is wrong with tag

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_uploaded_by_id")
    private User uploadedBy; //username or nickname of user who send the message

    @Column(name = "message_id")
    @NotNull
    @Min(0)
    private Integer messageId;


    @Column(name = "message_text")
    @NotNull
    private String messageText;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "file_id")
    private Long fileId; // todo check what happens if message was deleted or edited

    @Column(name = "ocr_result")
    private String ocrResult;

    @Column(name = "chat_id")
    private String chatId;
}
