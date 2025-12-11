package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Signup;
import com.example.onlinetutors.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {

    void handleCreateUser(User user, MultipartFile file);
    List<User> getAllUsers();
    User getUserById(Long id);
    void handleEditUser(User user, MultipartFile file);
    void handleUserDeleteId(Long id);
    User getUserByEmail(String email);
    void signupUser(Signup signup);
    String verifyUser(String token);
    void sendResetLink(String email);
    void resetPassword(String token, String newPassword);
    List<User> getUsersByRoleId(Long roleId);
}
