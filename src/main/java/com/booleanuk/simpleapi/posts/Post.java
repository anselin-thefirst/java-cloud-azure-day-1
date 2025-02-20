package com.booleanuk.simpleapi.posts;

import com.booleanuk.simpleapi.comments.Comment;
import com.booleanuk.simpleapi.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
// @JsonIgnoreProperties({"users"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String beer;
    @Column
    private String review;
    @Column
    private int rating;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id", "username"})
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public Post(String title, String beer, String review, int rating) {
        this.title = title;
        this.beer = beer;
        this.review = review;
        this.rating = rating;
    }

    public Post(int id) {
        this.id = id;
    }
}