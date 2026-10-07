package com.ead.authuser.services.Impl;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCourseServiceImpl implements UserCourserService {

    final UserCourseRepository userCourseRepository;
}
