package com.booleanuk.simpleapi.comments;

import com.booleanuk.simpleapi.posts.Post;
import com.booleanuk.simpleapi.posts.PostRepository;
import com.booleanuk.simpleapi.responses.*;
import com.booleanuk.simpleapi.users.User;
import com.booleanuk.simpleapi.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("{postId}/comments")
public class CommentController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private CommentResponse commentResponse = new CommentResponse();
    private CommentListResponse commentListResponse = new CommentListResponse();

    @GetMapping()
    public ResponseEntity<Response<?>> getAllPostComments(@PathVariable int postId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        if (post == null) {
            this.errorResponse.set("No post with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.commentListResponse.set(this.commentRepository.findByPost(post));
        return ResponseEntity.ok(commentListResponse);
    }

    @PostMapping("{commenterId}")
    public ResponseEntity<Response<?>> createNewComment(@PathVariable int postId, @PathVariable int commenterId, @RequestBody Comment comment) {
        Post post = this.postRepository.findById(postId).orElse(null);
        if (post == null) {
            this.errorResponse.set("No post with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        User commenter = this.userRepository.findById(commenterId).orElse(null);
        if (commenter == null) {
            this.errorResponse.set("No user with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        if (comment.getContent() == null) {
            this.errorResponse.set("Could not create a new comment, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        comment.setPost(post);
        comment.setCommenter(commenter);
        this.commentResponse.set(this.commentRepository.save(comment));
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<Response<?>> updateComment(@PathVariable int commentId, @RequestBody Comment comment) { // post id?
        Comment commentToUpdate = this.commentRepository.findById(commentId).orElse(null);
        if (commentToUpdate == null) {
            this.errorResponse.set("No comment with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (comment.getContent() == null) {
            this.errorResponse.set("Could not update the specified comment, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        commentToUpdate.setContent(comment.getContent());
        commentToUpdate.setUpdatedAt(LocalDateTime.now());
        this.commentResponse.set(this.commentRepository.save(commentToUpdate));
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Response<?>> deleteComment(@PathVariable int commentId) {
        Comment commentToDelete = this.commentRepository.findById(commentId).orElse(null);
        if (commentToDelete == null) {
            errorResponse.set("No comment with that id was found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.commentRepository.delete(commentToDelete);
        this.commentResponse.set(commentToDelete);
        return ResponseEntity.ok(commentResponse);
    }

}
