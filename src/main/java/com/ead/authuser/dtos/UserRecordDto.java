package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;

public record UserRecordDto(@JsonView(UserView.UserPost.class)
                            String username,
                            @JsonView(UserView.UserPost.class)
                            String email,
                            @JsonView({UserView.UserPost.class, UserView.UserPasswordPut.class})
                            String password,
                            @JsonView(UserView.UserPasswordPut.class)
                            String oldPassowrd,
                            @JsonView({UserView.UserPost.class, UserView.UserPut.class})
                            String fullName,
                            @JsonView({UserView.UserPost.class, UserView.UserPut.class})
                            String phoneNumber,
                            @JsonView(UserView.UserImagePut.class)
                            String imagUrl) {

    public interface UserView {
        interface UserPost {}
        interface UserPut {}
        interface UserPasswordPut {}
        interface UserImagePut {}
    }
}
