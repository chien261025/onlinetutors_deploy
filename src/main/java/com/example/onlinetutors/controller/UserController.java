package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.util.enumclass.StatusUserEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final String REDIRECT_USER_LIST = "redirect:/admin/user";

    @GetMapping("/admin/user/create")
    public String getUserPage(Model model) {
        User user = new User();
        model.addAttribute("newUser" , user);
        return "admin/user/createUser";
    }

    @GetMapping("/admin/user")
    public String getUserListPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("ACTIVE", StatusUserEnum.ACTIVE);
        model.addAttribute("BANNED", StatusUserEnum.BANNED);
        return "admin/user/userList";
    }

    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model,
                                 @ModelAttribute("newUser") User user,
                                 @RequestParam("imageUser")MultipartFile file) {
        this.userService.handleCreateUser(user, file);
        return REDIRECT_USER_LIST;
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getEditUserPage(Model model, @ModelAttribute("id") Long id) {
        User user = null;
        try {
            user = this.userService.getUserById(id);
        } catch (
                RuntimeException e) {
            log.info(e.getMessage());
        }
        model.addAttribute("editUser", user);
        return "admin/user/editUser";
    }

    @PostMapping("/admin/user/editUser")
    public String editUserPage(Model model,
                               @ModelAttribute("editUser") User user,
                               @RequestParam("imageUser")MultipartFile file) {
        this.userService.handleEditUser(user, file);
        return REDIRECT_USER_LIST;
    }

    @PostMapping("/admin/user/delete/{id}")
    public String postDeleteUser(@PathVariable("id") long id) {
        this.userService.handleUserDeleteId(id);
        return REDIRECT_USER_LIST;
    }

}
