package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import com.example.gencybersocialmediaapi.utils.FlatFileOps;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    final Stack<Post> postList = new Stack<>();

    public PostFileDAO(@Value("data/bankRobberyPosts.json") String filename, ObjectMapper objectMapper) throws IOException {
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
    public Post createPost(Post post) throws IOException {
        synchronized (postList) {
            Post newPost = new Post();
            newPost.setUsername(post.getUsername());
            newPost.setPostContent(post.getPostContent());
            if (post.getPhotoURL() != null) {
                newPost.setPhotoURL(post.getPhotoURL());
            }
            postList.add(post);
            save();
            return newPost;
        }
    }

    @Override
    public Stack<Post> getAllPosts() {
        return postList;
    }

    @Override
    public boolean deletePostByUsername(String username) throws IOException {
        synchronized (postList) {
            Post post = new Post();
            if (post.getUsername() == username) {
                postList.remove(post);
                return save();
            }
            return false;
        }
    }

    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename), postList);
        return false;
    }
    private void load() throws IOException {
        FlatFileOps.ensureDataFileExists(filename, "[]");
        List<Post> posts = objectMapper.readValue(new File(filename), new TypeReference<>() {});
        postList.addAll(posts);
    }
}
