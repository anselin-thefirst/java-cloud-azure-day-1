package com.booleanuk.simpleapi.posts;

import com.booleanuk.simpleapi.responses.ErrorResponse;
import com.booleanuk.simpleapi.responses.PostListResponse;
import com.booleanuk.simpleapi.responses.PostResponse;
import com.booleanuk.simpleapi.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private PostResponse postResponse = new PostResponse();
    private PostListResponse postListResponse = new PostListResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllPosts() {
        this.postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createNewPost(@RequestBody Post post) { //path variable user id?
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        if (post.getTitle() == null || post.getBeer() == null || post.getReview() == null) {
            this.errorResponse.set("Could not create a new post, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.postResponse.set(this.postRepository.save(post));
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            this.errorResponse.set("No post with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElse(null);
        if (postToUpdate == null) {
            this.errorResponse.set("No post with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (post.getTitle() == null || post.getBeer() == null || post.getReview() == null) {
            this.errorResponse.set("Could not update the specified post, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setBeer(post.getBeer());
        postToUpdate.setReview(post.getReview());
        postToUpdate.setRating(post.getRating());
        postToUpdate.setUpdatedAt(LocalDateTime.now());
        this.postResponse.set(this.postRepository.save(postToUpdate));
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            errorResponse.set("No post with that id was found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(postToDelete);
        this.postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }

}
