package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.exceptions.CustomNotFoundException;
import com.m42hub.m42hub_api.file.service.ImgBBService;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInterestProjectRoleService userInterestProjectRoleService;
    private final AuthenticationManager authenticationManager;
    private final SystemRoleRepository systemRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ImgBBService imgBBService;

    // save() e saveWithRole()
    // REF: https://github.com/m42hub/m42hub-api/blob/main/src/main/java/com/m42hub/m42hub_api/user/service/UserService.java#L49
    @Transactional
    public User save(User user) {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public UserSaveResult saveWithRole(User user) {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        SystemRole systemRole = systemRoleRepository.findById(savedUser.getSystemRoleId())
                .orElseThrow(() -> new CustomNotFoundException("SystemRole não encontrado"));

        return new UserSaveResult(savedUser, systemRole);
    }

    // Gambiarra para amarrar em uma unica transação
    public record UserSaveResult(User user, SystemRole systemRole) {
    }


    @Transactional(readOnly = true)
    public Map<UUID, User> findAllByIds(List<UUID> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<User> update(User userInfo, UUID userId, List<Long> roleIdsToUpdate) {
        return userRepository.findById(userId).map(user -> {
            Optional.ofNullable(userInfo.getFirstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(userInfo.getLastName()).ifPresent(user::setLastName);
            Optional.ofNullable(userInfo.getBiography()).ifPresent(user::setBiography);
            Optional.ofNullable(userInfo.getDiscord()).ifPresent(user::setDiscord);
            Optional.ofNullable(userInfo.getLinkedin()).ifPresent(user::setLinkedin);
            Optional.ofNullable(userInfo.getGithub()).ifPresent(user::setGithub);
            Optional.ofNullable(userInfo.getPersonalWebsite()).ifPresent(user::setPersonalWebsite);

            if (roleIdsToUpdate != null) {
                userInterestProjectRoleService.addRelations(userId, roleIdsToUpdate);
            }

            return userRepository.save(user);
        });
    }

    @Transactional
    public Optional<User> changeProfilePic(MultipartFile file, UUID userId) {
        return userRepository.findById(userId).map(user -> {
            String imageUrl = imgBBService.uploadImage(file);

            user.setProfilePicUrl(imageUrl);
            return userRepository.save(user);
        });
    }

    @Transactional
    public Optional<User> changeProfileBanner(MultipartFile file, UUID userId) {
        return userRepository.findById(userId).map(user -> {
            String imageUrl = imgBBService.uploadImage(file);

            user.setProfileBannerUrl(imageUrl);
            return userRepository.save(user);
        });
    }

    @Transactional
    public Optional<User> changePassword(String oldPassword, String newPassword, UUID userId) {
        return userRepository.findById(userId).map(user -> {
            UsernamePasswordAuthenticationToken usernameAndPassword = new UsernamePasswordAuthenticationToken(user.getUsername().toLowerCase(), oldPassword);
            authenticationManager.authenticate(usernameAndPassword);

            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        });
    }

    @Transactional
    public Optional<User> changeStatus(UUID id, boolean isActive) {
        return userRepository.findById(id).map(user -> {
            user.setIsActive(isActive);
            userRepository.save(user);
            return user;
        });
    }
}