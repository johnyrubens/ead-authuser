package com.ead.authuser.repositories;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class UserCourseRepositoryTests {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllUserCourseIntoUserReturnsAllCoursesOnlyForRequestedUser() {
        UserModel user = persistUser("student", UserStatus.ACTIVE, UserType.STUDENT);
        UserModel otherUser = persistUser("other", UserStatus.ACTIVE, UserType.STUDENT);
        UserCourseModel firstCourse = persistUserCourse(user, UUID.randomUUID());
        UserCourseModel secondCourse = persistUserCourse(user, UUID.randomUUID());
        persistUserCourse(otherUser, UUID.randomUUID());
        flushAndClear();

        List<UserCourseModel> result = userCourseRepository.findAllUserCourseIntoUser(user.getUserId());

        assertThat(result).extracting(UserCourseModel::getId)
                .containsExactlyInAnyOrder(firstCourse.getId(), secondCourse.getId());
        assertThat(result).extracting(UserCourseModel::getCourseId)
                .containsExactlyInAnyOrder(firstCourse.getCourseId(), secondCourse.getCourseId());
        assertThat(result).allSatisfy(course ->
                assertThat(course.getUser().getUserId()).isEqualTo(user.getUserId()));
    }

    @Test
    void findAllUserCourseIntoUserReturnsOnlyRequestedUsersEnrollmentForSharedCourse() {
        UserModel user = persistUser("student", UserStatus.ACTIVE, UserType.STUDENT);
        UserModel otherUser = persistUser("other", UserStatus.ACTIVE, UserType.STUDENT);
        UUID sharedCourseId = UUID.randomUUID();
        UserCourseModel enrollment = persistUserCourse(user, sharedCourseId);
        persistUserCourse(otherUser, sharedCourseId);
        flushAndClear();

        List<UserCourseModel> result = userCourseRepository.findAllUserCourseIntoUser(user.getUserId());

        assertThat(result).singleElement().satisfies(course -> {
            assertThat(course.getId()).isEqualTo(enrollment.getId());
            assertThat(course.getCourseId()).isEqualTo(sharedCourseId);
            assertThat(course.getUser().getUserId()).isEqualTo(user.getUserId());
        });
    }

    @Test
    void findAllUserCourseIntoUserReturnsEmptyWhenUserHasNoCourses() {
        UserModel user = persistUser("student", UserStatus.ACTIVE, UserType.STUDENT);
        UserModel otherUser = persistUser("other", UserStatus.ACTIVE, UserType.STUDENT);
        persistUserCourse(otherUser, UUID.randomUUID());
        flushAndClear();

        assertThat(userCourseRepository.findAllUserCourseIntoUser(user.getUserId())).isEmpty();
    }

    @Test
    void findAllUserCourseIntoUserReturnsEmptyWhenUserDoesNotExist() {
        UserModel otherUser = persistUser("other", UserStatus.ACTIVE, UserType.STUDENT);
        persistUserCourse(otherUser, UUID.randomUUID());
        flushAndClear();

        assertThat(userCourseRepository.findAllUserCourseIntoUser(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findAllUserCourseIntoUserReturnsCoursesRegardlessOfUserStatusAndType() {
        UserModel user = persistUser("admin", UserStatus.BLOCKED, UserType.ADMIN);
        UserCourseModel enrollment = persistUserCourse(user, UUID.randomUUID());
        flushAndClear();

        List<UserCourseModel> result = userCourseRepository.findAllUserCourseIntoUser(user.getUserId());

        assertThat(result).extracting(UserCourseModel::getId).containsExactly(enrollment.getId());
    }

    private UserModel persistUser(String username, UserStatus status, UserType type) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPassword("test-password");
        user.setFullName("Test User " + username);
        user.setUserStatus(status);
        user.setUserType(type);
        user.setCreationDate(LocalDateTime.of(2026, 1, 1, 12, 0));
        user.setLastUpdateDate(user.getCreationDate());
        return entityManager.persist(user);
    }

    private UserCourseModel persistUserCourse(UserModel user, UUID courseId) {
        return entityManager.persist(new UserCourseModel(null, courseId, user));
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
