package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Component
public class PostFileDAO implements PostDAO {
    private static final Logger LOG = Logger.getLogger(PostFileDAO.class.getName());
    private final ObjectMapper objectMapper;
    private final String filename;
    private final ArrayList<Post> postList = new ArrayList<>();
    private final UserFileDAO userFileDAO;
    public PostFileDAO(@Value("data/posts.json") String filename, ObjectMapper objectMapper, UserFileDAO userFileDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.userFileDAO = userFileDAO;
        this.filename = filename;
        load();
    }
    @Override
    public Post createPost(Post post) throws IOException {
        User user = userFileDAO.findByUsername(post.getUsername());
        if (user != null) {
            List<Post> userPosts = user.getUserPosts();
            userPosts.add(post);
            userFileDAO.updateUser(user);
            save();
        }
        return post;
    }
    @Override
    public ArrayList<Post> getPostsByUser(User user)  {
        ArrayList<Post> userPosts = new ArrayList<>();
        for (Post post : postList) {
            if (post.getUsername().equals(user.getUsername())) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }

    @Override
    public ArrayList<Post> getAllPosts() {
        return postList;
    }

    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), postList);
    }
    private List<Post> load() throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
        } else {
            return new ArrayList<>();
        }
    }
}
