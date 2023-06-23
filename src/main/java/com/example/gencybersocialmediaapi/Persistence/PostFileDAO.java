package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.Post;
import com.example.gencybersocialmediaapi.Model.User;
import com.example.gencybersocialmediaapi.utils.FlatFileOps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Component
public class PostFileDAO implements PostDAO {
    private static final Logger LOG = Logger.getLogger(PostFileDAO.class.getName());
    private final ObjectMapper objectMapper;
    private final String filename;
    ArrayList<Post> postList = new ArrayList<>();
    final Map<String, Post> postMap = new HashMap<>();
    private final UserFileDAO userFileDAO;
    public PostFileDAO(@Value("data/posts.json") String filename, ObjectMapper objectMapper, UserFileDAO userFileDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.filename = filename;
        load();
        this.userFileDAO = userFileDAO;
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
    private void load() throws IOException {
        FlatFileOps.ensureDataFileExists(filename, "[{\"User\": {...}, \"samplePostContent\": \"content\", \"timeStamp\": \"sampleTimeStamp\"}]");
        Post[] posts = objectMapper.readValue(new File(filename), Post[].class);
        for (Post post : posts) {
            postMap.put(post.getUsername(), post);
        }
    }
}
