package com.example.onlinetutors.service.impl;

import com.example.onlinetutors.model.PasswordResetToken;
import com.example.onlinetutors.model.Signup;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.model.VerificationTokens;
import com.example.onlinetutors.repository.PasswordResetTokenRepository;
import com.example.onlinetutors.repository.RoleRepository;
import com.example.onlinetutors.repository.SignupRepository;
import com.example.onlinetutors.repository.UserRepository;
import com.example.onlinetutors.repository.VerificationTokensRepository;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.util.enumclass.StatusUserEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${spring.sendgrid.verification-link}")
    private String link;

    @Value("${spring.sendgrid.reset-password-link}")
    private String linkResetPassword;

    @Value("${spring.sendgrid.reset_templateId}")
    private String resetTemplateId;

    @Value("${spring.sendgrid.templateId}")
    private String templateId;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignupRepository signupRepository;
    private final VerificationTokensRepository verificationTokensRepository;
    private final EmailService emailService;
    private final FileService uploadFileService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           SignupRepository signupRepository,
                           VerificationTokensRepository verificationTokensRepository,
                           FileService uploadFileService,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.signupRepository = signupRepository;
        this.verificationTokensRepository = verificationTokensRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.uploadFileService = uploadFileService;
    }


    @Override
    public void handleCreateUser(User user, MultipartFile file) {
        log.info("Creating user: {}", user.getName());
        String uploadFile = this.uploadFileService.handleSaveUploadFile(file, "uploads/adminImg/images");
        user.setAvatar(uploadFile);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(this.roleRepository.findByName(user.getRole().getName()));
        this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            if (user.get().getStatusUser() == StatusUserEnum.ACTIVE) {
                log.info("User found: {}", user.get().getName());
                return user.get();
            }
        } else {
            log.error("User with id: {} not found", id);
            throw new RuntimeException("User not found");
        }
        log.error("User with id: {} is not active", id);
        throw new RuntimeException("User is not active");
    }

    @Override
    public void handleEditUser(User user, MultipartFile file) {
        log.info("Editing user with id: {}", user.getId());
        User existingUser = getUserByEmail(user.getEmail());
        if (existingUser != null) {
            if (!file.isEmpty()) {
                String uploadFile = this.uploadFileService.handleSaveUploadFile(file, "uploads/adminImg/images");
                existingUser.setAvatar(uploadFile);
            }
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setRole(this.roleRepository.findByName(user.getRole().getName()));
            existingUser.setStatusUser(user.getStatusUser());
            if (user.getProfileDescription() != null) {
                existingUser.setProfileDescription(user.getProfileDescription());
            }
            if (user.getQualification() != null) {
                existingUser.setQualification(user.getQualification());
            }
            this.userRepository.save(existingUser);
        } else {
            log.error("Cannot edit user. User with id: {} not found", user.getId());
            throw new RuntimeException("User not found for editing");
        }
    }

    @Override
    public void handleUserDeleteId(Long id) {
        User user = getUserById(id);
        user.setStatusUser(
                StatusUserEnum.INACTIVE
        );
        this.userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("User found: {}", user.get().getName());
            return user.get();
        } else {
            log.error("User with email: {} not found", email);
            throw new RuntimeException("User not found for editing");
        }
    }

    @Override
    @Transactional
    public void signupUser(Signup signup) {
        if (userRepository.existsByEmail(signup.getEmail())) {
            log.error("Email already in use: {}", signup.getEmail());
            throw new RuntimeException("Email is already in use");
        }
        log.info("Signing up user: {}", signup.getEmail());
        signup.setPassword(passwordEncoder.encode(signup.getPassword()));
        signupRepository.save(signup);

        // tạo token xác thực và gửi email xác thực ở đây
        String token = java.util.UUID.randomUUID().toString();
        VerificationTokens verificationToken = new VerificationTokens(token, signup,
                java.time.LocalDateTime.now().plusHours(24));
        verificationTokensRepository.save(verificationToken);

        // tao link xac thuc
        String verifyUrl = link + "?secretCode=" + token;

        emailService.emailVerificationCode(signup.getEmail(), signup.getName(), verifyUrl, templateId);
        log.info("Verification email sent to: {}", signup.getEmail());
    }

    @Override
    public String verifyUser(String token) {
        Optional<VerificationTokens> optionalToken = verificationTokensRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            VerificationTokens tokenSignup = optionalToken.get();
            if (tokenSignup.getExpiryDate().isAfter(java.time.LocalDateTime.now())) {
                Signup signup = tokenSignup.getSignup();
                User user = new User();
                user.setName(signup.getName());
                user.setEmail(signup.getEmail());
                user.setPassword(signup.getPassword());
                user.setStatusUser(StatusUserEnum.ACTIVE);
                user.setRole(roleRepository.findByName("PARENT"));
                userRepository.save(user);
                // xoa token sau khi xac thuc
                verificationTokensRepository.delete(tokenSignup);
                log.info("User verified and created: {}", user.getName());
                return "User verified successfully";
            } else {
                log.error("Verification token expired for token: {}", token);
                return "Verification token has expired";
            }
        }
        return "Invalid Token!";
    }

    @Override
    public List<User> getUsersByRoleId(Long id) {
        return this.userRepository.findByRole_Id(id);
    }

    public void sendResetLink(String email) {
        User user = getUserByEmail(email);
        if (user == null) {
            log.error("User with email: {} not found for password reset", email);
            throw new RuntimeException("User not found");
        }
        // tạo token
        String token = java.util.UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, email, LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(passwordResetToken);
        // tạo link reset
        String resetLink = linkResetPassword + "?resetToken=" + token;
        emailService.emailResetPassword(email, user.getName(), resetLink, resetTemplateId);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken byToken = passwordResetTokenRepository.findByToken(token);
        if (byToken == null) {
            log.error("Invalid password reset token: {}", token);
            throw new RuntimeException("Invalid token");
        }
        if (byToken.getExpiryTimeInMinutes().isBefore(LocalDateTime.now())) {
            log.error("Password reset token expired: {}", token);
            throw new RuntimeException("Token has expired");
        }
        User user = getUserByEmail(byToken.getEmail());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // xoa token sau khi reset
        passwordResetTokenRepository.delete(byToken);
        log.info("Password reset successfully for user: {}", user.getEmail());
    }
}

