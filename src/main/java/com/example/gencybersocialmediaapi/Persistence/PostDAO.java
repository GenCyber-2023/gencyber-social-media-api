package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public interface PostDAO {
    Post createPost(Post post) throws IOException;
    ArrayList<Post> getPostsByUser(User user);
    ArrayList<Post> getAllPosts();

}
