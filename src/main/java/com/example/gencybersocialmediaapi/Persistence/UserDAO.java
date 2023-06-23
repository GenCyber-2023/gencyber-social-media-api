package com.example.gencybersocialmediaapi.Persistence;


import com.example.gencybersocialmediaapi.Model.User;

import java.io.IOException;
import java.util.ArrayList;

public interface UserDAO {
    //Create a User
    User createUser(User user) throws IOException;

    //Find a user based on username
    User findByUsername(String username) throws IOException;

    //Update a user's information
    User updateUser(User user) throws IOException;

    //Check is user has admin privileges
    boolean isAdmin(String username, String password) throws IOException;

    //Check if user is a customer
    boolean isUser(String username, String password) throws IOException;

    ArrayList<User> getUsers(String userInfo) throws IOException;

    ArrayList<User> getUsers() throws IOException;
    boolean deleteUser(String username) throws IOException;
}
