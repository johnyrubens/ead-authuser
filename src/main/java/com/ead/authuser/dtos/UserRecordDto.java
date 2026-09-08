package com.ead.authuser.dtos;

import com.ead.authuser.validations.PasswordConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserRecordDto(@NotBlank(groups = UserView.UserPost.class, message = "Username is mandatory")
                            @Size(groups = UserView.UserPost.class, min = 4, max = 50, message = "Size must be between 4 and 50")
                            @JsonView(UserView.UserPost.class)
                            String username,

                            @NotBlank(groups = UserView.UserPost.class, message = "email is mandatory")
                            @Email(groups = UserView.UserPost.class, message = "Email must be in teh expected format")
                            @JsonView(UserView.UserPost.class)
                            String email,

                            @NotBlank(groups = {UserView.UserPost.class, UserView.UserPasswordPut.class}, message = "Password is mandatory")
                            @Size(groups = {UserView.UserPost.class, UserView.UserPasswordPut.class}, min = 6, max = 20, message = "Size must be between 4 and 20")
                            @PasswordConstraint(groups = {UserView.UserPost.class, UserView.UserPasswordPut.class})
                            @JsonView({UserView.UserPost.class, UserView.UserPasswordPut.class})
                            String password,

                            @NotBlank(groups = UserView.UserPasswordPut.class, message = "Password is mandatory")
                            @Size(groups = UserView.UserPasswordPut.class, min = 6, max = 20, message = "Size must be between 4 and 20")
                            @PasswordConstraint(groups = UserView.UserPasswordPut.class)
                            @JsonView(UserView.UserPasswordPut.class)
                            String oldPassword,

                            @NotBlank(groups = {UserView.UserPost.class, UserView.UserPut.class}, message = "Full name is mandatory")
                            @JsonView({UserView.UserPost.class, UserView.UserPut.class})
                            String fullName,

                            @JsonView({UserView.UserPost.class, UserView.UserPut.class})
                            String phoneNumber,

                            @NotBlank(groups = UserView.UserImagePut.class, message = "Image URL is mandatory")
                            @JsonView(UserView.UserImagePut.class)
                            String imageUrl) {

    public interface UserView {
        interface UserPost {}
        interface UserPut {}
        interface UserPasswordPut {}
        interface UserImagePut {}
    }
}
