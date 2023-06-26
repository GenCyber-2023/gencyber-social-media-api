package com.example.gencybersocialmediaapi.Persistence;

import com.example.gencybersocialmediaapi.Model.User;
import com.example.gencybersocialmediaapi.utils.FlatFileOps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.logging.Logger;

@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    final Map<String, User> userMap = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final String filename;

    ArrayList<User> userList = new ArrayList<>();

    public UserFileDAO(@Value("data/users.json") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }
    private static final String[] profilePictureURLArray = {
            "https://www.svgrepo.com/show/446530/avatar.svg",
            "https://www.svgrepo.com/show/446474/avatar.svg",
            "https://www.svgrepo.com/show/446487/avatar.svg",
            "https://www.svgrepo.com/show/446476/avatar-portrait.svg",
            "https://www.svgrepo.com/show/446519/avatar.svg",
            "https://www.svgrepo.com/show/446514/avatar.svg",
            "https://www.svgrepo.com/show/446485/avatar.svg",
    };
    @Override
    public User createUser(User inputUser) throws IOException {
        synchronized (userMap) {
            if (userMap.containsKey(inputUser.getUsername())) {
                throw new FileAlreadyExistsException("Username already exists");
            }
            User newUser = new User();
            String randomProfilePictureURL = getRandomProfilePictureURL();
            List<String> userPosts = new ArrayList<>();
            newUser.setUsername(inputUser.getUsername());
            newUser.setPassword(inputUser.getPassword());
            newUser.setName(inputUser.getName());
            newUser.setProfilePictureURL(randomProfilePictureURL);
            userMap.put(newUser.getUsername(), newUser);
            save();
            return newUser;
        }
    }
    private String getRandomProfilePictureURL() {
        Random random = new Random();
        int randomIndex = random.nextInt(profilePictureURLArray.length);
        return profilePictureURLArray[randomIndex];
    }

    private boolean save() throws IOException {
        userList = getUsers();
        objectMapper.writeValue(new File(filename), userList);
        return true;
    }
    @Override
    public User findByUsername(String username) throws IOException {
        synchronized (userMap) {
            return (userMap.getOrDefault(username, null));
        }
    }
    @Override
    public User updateUser(User updateUser) throws IOException {
        synchronized (userMap) {
            if (!userMap.containsKey(updateUser.getUsername())) {
                throw new FileNotFoundException("User does not exist");
            }
            User currentUser = userMap.get(updateUser.getUsername());
            if (updateUser.getUsername() != null) currentUser.setUsername(updateUser.getUsername());
            if (updateUser.getPassword() != null) currentUser.setPassword(updateUser.getPassword());

            userMap.put(currentUser.getUsername(), currentUser);
            save();
            return currentUser;
        }
    }

    @Override
    public boolean isAdmin(String username, String password) {
        return username.toLowerCase().contains("admin");
    }
    @Override
    public boolean isUser(String username, String password) throws IOException {
        return !this.isAdmin(username, password);
    }
    @Override
    public ArrayList<User> getUsers() throws IOException {
        return getUsers(null);
    }
    @Override
    public ArrayList<User> getUsers(String userInfo) {
        ArrayList<User> userArrayList = new ArrayList<>();

        if (userInfo == null) {
            userArrayList.addAll(userMap.values());
        } else {
            for (User user : userMap.values()) {
                if (user.toString().contains(userInfo)) {
                    userArrayList.add(user);
                }
            }
        }
        return userArrayList;
    }

    private void load() throws IOException {
        FlatFileOps.ensureDataFileExists(filename, "[{\"username\": \"admin\", \"password\": \"admin\"}]");
        User[] users = objectMapper.readValue(new File(filename), User[].class);
        for (User user : users) {
            userMap.put(user.getUsername(), user);
        }
    }
}
