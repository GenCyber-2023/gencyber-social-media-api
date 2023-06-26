package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import com.example.gencybersocialmediaapi.utils.FlatFileOps;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


@Component
public class PostFileDAO implements PostDAO {
    private static final Logger LOG = Logger.getLogger(PostFileDAO.class.getName());
    private final ObjectMapper objectMapper;
    private final String filename;
    final ArrayList<Post> postList = new ArrayList<>();

    public PostFileDAO(@Value("data/postList.json") String filename, ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.filename = filename;
        load();
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
    public Post createPost(String username, String postContent) throws IOException {
        synchronized (postList) {
            Post post = new Post();
            post.setUsername(username);
            post.setPostContent(postContent);
            postList.add(post);
            save();
            return post;
        }
    }

    @Override
    public ArrayList<Post> getAllPosts() {
        return postList;
    }

    private void save() throws IOException {
        objectMapper.writeValue(new File(filename), postList);
    }
    private void load() throws IOException {
        FlatFileOps.ensureDataFileExists(filename, "[]");
        List<Post> posts = objectMapper.readValue(new File(filename), new TypeReference<>() {});
        postList.addAll(posts);
    }
}
