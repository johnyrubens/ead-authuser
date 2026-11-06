package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    final UserService userService;
    final UserValidator userValidator;

   @PostMapping("/signup")
   public ResponseEntity<Object> resisterUser(@RequestBody
                                               @Validated(UserRecordDto.UserView.UserPost.class)
                                               @JsonView(UserRecordDto.UserView.UserPost.class) UserRecordDto userRecordDto,
                                              Errors errors) {

       userValidator.validate(userRecordDto, errors);
       if(errors.hasErrors()){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
       }
       return ResponseEntity.status(HttpStatus.CREATED).body(userService.resisterUser(userRecordDto));
   }

}
