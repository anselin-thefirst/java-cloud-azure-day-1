package com.booleanuk.simpleapi.comments;

import com.booleanuk.simpleapi.posts.Post;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
// @JsonIgnoreProperties({"users"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String content;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIncludeProperties({"id", "title"})
    private Post post;

    public Comment(String content) {
        this.content = content;
    }

    public Comment(int id) {
        this.id = id;
    }
}
