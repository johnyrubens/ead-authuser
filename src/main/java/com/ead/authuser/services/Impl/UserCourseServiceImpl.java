package com.ead.authuser.services.Impl;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserCourseServiceImpl implements UserCourserService {

    final UserCourseRepository userCourseRepository;

    @Override
    public boolean existsByUseAndCourseId(UserModel userModel, UUID uuid) {
        return userCourseRepository.existsByUserAndCourseId(userModel, uuid);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        return userCourseRepository.save(userCourseModel);
    }


}
