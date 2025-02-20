package com.booleanuk.simpleapi.comments;

import com.booleanuk.simpleapi.posts.Post;
import com.booleanuk.simpleapi.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByUser(User user);
    List<Comment> findByPost(Post post);
}
