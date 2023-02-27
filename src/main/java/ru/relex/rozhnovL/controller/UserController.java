package ru.relex.rozhnovL.controller;

import generator.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.requests.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.User;


@RestController
public class UserController {

    @Autowired
    Services services;


    /**
     * (USER) Sign Up
     * @param request
     * @return
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@RequestBody RegistrationRequest request) {
        System.out.println("username: " + request.username + " email: " + request.email);
        String secretKey = Generator.generateSecretKey(request.username, request.email);

        User newUser = new User(request.username, request.email, secretKey, false);
        services.user.save(newUser);
        return "{ \"secret_key\": \"" + secretKey + "\"}";
    }
}
