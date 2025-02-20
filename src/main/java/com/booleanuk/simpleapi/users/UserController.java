package com.booleanuk.simpleapi.users;

import com.booleanuk.simpleapi.posts.Post;
import com.booleanuk.simpleapi.posts.PostRepository;
import com.booleanuk.simpleapi.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private UserResponse userResponse = new UserResponse();
    private UserListResponse userListResponse = new UserListResponse();
    private PostListResponse postListResponse = new PostListResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers() {
        this.userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (user.getUsername() == null || user.getEmail() == null) {
            this.errorResponse.set("Could not create a new user, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.userResponse.set(this.userRepository.save(user));
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            this.errorResponse.set("No user with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            this.errorResponse.set("No user with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (user.getUsername() == null || user.getEmail() == null) {
            this.errorResponse.set("Could not update the specified user, please check all fields are correct");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setUpdatedAt(LocalDateTime.now());
        this.userResponse.set(this.userRepository.save(userToUpdate));
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUserById(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            this.errorResponse.set("No user with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);
        this.userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<Response<?>> getUserPosts(@PathVariable int id) {
        User userToFind = this.userRepository.findById(id).orElse(null);
        if (userToFind == null) {
            this.errorResponse.set("No user with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<Post> posts = this.postRepository.findByUser(userToFind);
        this.postListResponse.set(posts);
        return ResponseEntity.ok(postListResponse);
    }

}
