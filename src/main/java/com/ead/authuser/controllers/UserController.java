package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/users")

//@CrossOrigin(origins = "*",  maxAge = 3600)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       Pageable pageable,
                                                       @RequestParam(required = false) UUID courseId) {
        try {
            // Diferente da videoaula (Spring Boot 3.3.3), no Spring Boot 4
            // o Specification.and() não aceita uma specification nula.
            // Quando nenhum filtro de UserSpec é informado, usamos unrestricted()
            // para representar a ausência de filtros e evitar a exceção.

            Specification<UserModel> filters = spec;

            if (courseId != null) {
                Specification<UserModel> courseSpec =
                        SpecificationTemplate.userCourseId(courseId);

                filters = (spec == null)
                        ? courseSpec
                        : courseSpec.and(spec);
            }

            Page<UserModel> userModelPage = userService.findAll(filters, pageable);

            // Só funciona spring boot versao 3.
            /*Page<UserModel> userModelPage = (courseId != null)
                    ? userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable)
                    : userService.findAll(spec, pageable);*/

            if (!userModelPage.isEmpty()) {
                for(UserModel user : userModelPage.toList()) {
                    user.add(
                            linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel()
                    );
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(userModelPage);

        } catch (Exception e) {
            logger.error("Exception in getAllUsers", e);
            throw new RuntimeException(e);
        }

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

