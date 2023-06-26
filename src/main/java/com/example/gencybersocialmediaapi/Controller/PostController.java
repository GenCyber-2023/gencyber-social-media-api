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
    public ResponseEntity<Post> createPost(@PathVariable String username, @RequestBody String postContent) throws IOException {
        try {
            postContent = postContent.replaceAll("\"postContent\":\\s*", "")
                    .replaceAll("[{}\"]", "")
                    .replaceAll("\\\\n", "")
                    .trim();
            Post newPost = postDAO.createPost(username, postContent);
            return new ResponseEntity<>(newPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<ArrayList<Post>> getAllPosts() throws IOException {
        ArrayList<Post> postList = postDAO.getAllPosts();
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
}
