package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import java.util.UUID;

public interface UserCourserService {

    boolean existsByUseAndCourseId(UserModel userModel, UUID courseId);
    UserCourseModel save(UserCourseModel userCourseModel);
    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}
