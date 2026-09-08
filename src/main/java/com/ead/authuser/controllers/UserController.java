package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    /*@GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }*/

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(Pageable pageable) {
        Page<UserModel> userModelPage = userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody
                                             @Validated(UserRecordDto.UserView.UserPut.class)
                                             @JsonView(UserRecordDto.UserView.UserPut.class)
                                             UserRecordDto userRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDto, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody
                                                 @Validated(UserRecordDto.UserView.UserPasswordPut.class)
                                                 @JsonView(UserRecordDto.UserView.UserPasswordPut.class)
                                                 UserRecordDto userRecordDto) {

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (!userModelOptional.get().getPassword().equals(userRecordDto.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error : Mismatched Old Password");
        }
        userService.updatePassword(userRecordDto, userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody
                                              @Validated(UserRecordDto.UserView.UserImagePut.class)
                                              @JsonView(UserRecordDto.UserView.UserImagePut.class)
                                              UserRecordDto userRecordDto) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDto, userService.findById(userId).get()));
    }
}

