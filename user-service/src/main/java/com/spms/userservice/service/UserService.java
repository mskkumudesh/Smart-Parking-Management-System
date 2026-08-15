package com.spms.userservice.service;

import com.spms.userservice.dto.*;
import com.spms.userservice.entity.BookingRecord;
import com.spms.userservice.entity.Role;
import com.spms.userservice.entity.User;
import com.spms.userservice.exception.AuthenticationFailedException;
import com.spms.userservice.exception.DuplicateResourceException;
import com.spms.userservice.exception.ResourceNotFoundException;
import com.spms.userservice.repository.BookingRecordRepository;
import com.spms.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRecordRepository bookingRecordRepository;

    @Value("${spms.security.password-salt:spms-salt}")
    private String passwordSalt;

    public UserService(UserRepository userRepository, BookingRecordRepository bookingRecordRepository) {
        this.userRepository = userRepository;
        this.bookingRecordRepository = bookingRecordRepository;
    }

    public UserResponse register(UserRegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("A user with email " + req.getEmail() + " already exists");
        }
        User user = new User(req.getName(), req.getEmail(), hash(req.getPassword()), req.getPhone(), req.getRole());
        return UserResponse.from(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));
        if (!user.isActive() || !matches(req.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password");
        }
        // Demo token: coursework project, not a real JWT. Good enough for Postman-driven testing.
        String token = Base64.getEncoder().encodeToString((user.getEmail() + ":" + UUID.randomUUID()).getBytes());
        return new LoginResponse(token, UserResponse.from(user));
    }

    public UserResponse getById(Long id) {
        return UserResponse.from(findUserOrThrow(id));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        User user = findUserOrThrow(id);
        if (req.getName() != null) user.setName(req.getName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        return UserResponse.from(userRepository.save(user));
    }

    public void deactivate(Long id) {
        User user = findUserOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public Page<UserResponse> list(Role role, Pageable pageable) {
        Page<User> page = (role != null) ? userRepository.findByRole(role, pageable) : userRepository.findAll(pageable);
        return page.map(UserResponse::from);
    }

    public BookingRecord addHistory(Long userId, BookingRecordRequest req) {
        findUserOrThrow(userId); // ensures the user exists
        BookingRecord record = new BookingRecord();
        record.setUserId(userId);
        record.setSpaceId(req.getSpaceId());
        record.setVehicleId(req.getVehicleId());
        record.setStartTime(req.getStartTime());
        record.setEndTime(req.getEndTime());
        record.setStatus(req.getStatus());
        return bookingRecordRepository.save(record);
    }

    public Page<BookingRecord> getHistory(Long userId, Pageable pageable) {
        findUserOrThrow(userId);
        return bookingRecordRepository.findByUserIdOrderByStartTimeDesc(userId, pageable);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    private String hash(String raw) {
        return BCrypt.hashpw(raw + passwordSalt, BCrypt.gensalt());
    }

    private boolean matches(String raw, String hashed) {
        return BCrypt.checkpw(raw + passwordSalt, hashed);
    }
}
