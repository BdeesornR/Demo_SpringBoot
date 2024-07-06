package com.bdeesorn_r.demo_crud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bdeesorn_r.demo_crud.dto.CommonResponse;
import com.bdeesorn_r.demo_crud.dto.UserDto;
import com.bdeesorn_r.demo_crud.service.UserService;

import io.micrometer.common.util.StringUtils;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-user/{username}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable String username) {
        UserDto user = userService.getUser(username);

        if (user == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Record not found"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new CommonResponse("200", "Success", user), HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createUser(@RequestBody UserDto userDto) {
        if (StringUtils.isBlank(userDto.getUsername())) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Username is required"), HttpStatus.OK);
        }

        UserDto user = userService.createUser(userDto);

        if (user == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Username already exists"), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(new CommonResponse("200", "Success", user), HttpStatus.OK);
    }
    
    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Id is required"), HttpStatus.OK);
        }

        UserDto user = userService.updateUser(userDto);

        if (user == null) {
            return new ResponseEntity<>(new CommonResponse("400", "Failed: Record not found"), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(new CommonResponse("200", "Success"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new CommonResponse("200", "Success"), HttpStatus.OK);
    }
}
