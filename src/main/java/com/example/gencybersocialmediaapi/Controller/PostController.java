package com.example.gencybersocialmediaapi.Controller;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import com.example.gencybersocialmediaapi.Persistence.PostDAO;
import com.example.gencybersocialmediaapi.Persistence.UserDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


@RestController
@RequestMapping("post")
public class PostController {
    private final UserDAO userDAO;
    private final PostDAO postDAO;

    public PostController(PostDAO postDAO, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.postDAO = postDAO;
    }
    @PostMapping("/{username}/create")
    public ResponseEntity<Post> createPost(@PathVariable String username, @RequestBody Post post) throws IOException {
        try {
            Post newPost = new Post();
            newPost.setUsername(username);
            newPost.setPostContent(post.getPostContent());
            if (post.getPhotoURL() != null) {
                newPost.setPhotoURL(post.getPhotoURL());
            }
            return new ResponseEntity<>(postDAO.createPost(newPost), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Stack<Post>> getAllPosts() throws IOException {
        Stack<Post> postList = postDAO.getAllPosts();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ArrayList<Post>> getPostsByUser(@PathVariable String username) throws IOException {
        try {
            User user = userDAO.findByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(postDAO.getPostsByUser(user), HttpStatus.OK);
            } else {
                throw new IOException("User not found");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not retrieve posts");
        }
    }

    @DeleteMapping("/{username}/delete")
    public ResponseEntity<String> deletePost(@PathVariable String username) {
        try {
            postDAO.deletePostByUsername(username);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
