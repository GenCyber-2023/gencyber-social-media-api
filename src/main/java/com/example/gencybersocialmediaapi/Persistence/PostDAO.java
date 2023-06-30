package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
public interface PostDAO {
    ArrayList<Post> getPostsByUser(User user) throws IOException;
    Stack<Post> getAllPosts() throws IOException;
    Post createPost(Post post) throws IOException;
    boolean deletePostByUsername(String username) throws IOException;
}
