package com.ptit.elearningsecurity.service.user;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.UserMapper;
import com.ptit.elearningsecurity.data.request.UserRequest;
import com.ptit.elearningsecurity.data.response.UserPageableResponse;
import com.ptit.elearningsecurity.data.response.UserResponse;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @Override
    public UserPageableResponse findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();

        UserPageableResponse userPageableResponse = new UserPageableResponse();
        userPageableResponse.setData(userMapper.toUserResponses(users))
                .setTotalItems(userPage.getTotalElements())
                .setTotalPages(userPage.getTotalPages())
                .setCurrentPage(userPage.getNumber());
        return userPageableResponse;
    }

    @Override
    public UserResponse findByID(int userID) throws UserCustomException {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        return userMapper.toResponse(userOptional.get());
    }

    @Override
    public UserResponse create(UserRequest userRequest) throws UserCustomException {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserCustomException("User Email Exists", DataUtils.ERROR_USER_EXIST);
        }
        User user = userMapper.toPojo(userRequest);
        User userSaved = userRepository.save(user);
        return userMapper.toResponse(userSaved);
    }

    @Override
    public UserResponse update(int userID, UserRequest userRequest) throws UserCustomException {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        User user = userOptional.get();
        if(Objects.nonNull(userRequest.getFirstname()) && !"".equalsIgnoreCase(userRequest.getFirstname())) {
            user.setFirstname(userRequest.getFirstname());
        }
        if(Objects.nonNull(userRequest.getLastname()) && !"".equalsIgnoreCase(userRequest.getLastname())) {
            user.setLastname(userRequest.getLastname());
        }
        if(Objects.nonNull(userRequest.getEmail()) && !"".equalsIgnoreCase(userRequest.getEmail())) {
            user.setEmail(userRequest.getEmail());
        }
        user.setUpdatedAt(Instant.now());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse uploadAvatar(int userID, @RequestParam("image") MultipartFile image) throws UserCustomException, IOException {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        User user = userOptional.get();

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path userAvatar = Paths.get("userAvatar");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(userAvatar))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(userAvatar));
        }

        if(!user.getAvatar().equalsIgnoreCase("default.png")) {
            deleteImageResource("/images/userAvatar/" + user.getAvatar());
        }

        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(userAvatar)
                .resolve(Objects.requireNonNull(image.getOriginalFilename()));
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        user.setAvatar(image.getOriginalFilename());
        user.setUpdatedAt(Instant.now());
        return userMapper.toResponse(userRepository.save(user));
    }

    private void deleteImageResource(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath) + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }

    @Override
    public void delete(int userID) throws UserCustomException {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        userRepository.delete(userOptional.get());
    }
}
