package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    final UserService userService;

   @PostMapping("/signup")
    public ResponseEntity<Object> resisterUser(@RequestBody
                                               @JsonView(UserRecordDto.UserView.UserPost.class) UserRecordDto userRecordDto ) {
       if (userService.existsByUsername(userRecordDto.username())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
       }

       if (userService.existsByEmail(userRecordDto.email())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
       }
       return ResponseEntity.status(HttpStatus.CREATED).body(userService.resisterUser(userRecordDto));
   }

}
