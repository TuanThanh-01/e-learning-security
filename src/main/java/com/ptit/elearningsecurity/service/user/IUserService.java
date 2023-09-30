package com.ptit.elearningsecurity.service.user;

import com.ptit.elearningsecurity.data.request.UserRequest;
import com.ptit.elearningsecurity.data.response.UserPageableResponse;
import com.ptit.elearningsecurity.data.response.UserResponse;
import com.ptit.elearningsecurity.exception.UserCustomException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {
    UserPageableResponse findAll(Pageable pageable);
    UserResponse findByID(int userID) throws UserCustomException;
    UserResponse create(UserRequest userRequest) throws UserCustomException;
    UserResponse update(int userID, UserRequest userRequest) throws UserCustomException;
    UserResponse uploadAvatar(int userID, MultipartFile multipartFile) throws UserCustomException, IOException;
    void delete(int userID) throws UserCustomException;
}
