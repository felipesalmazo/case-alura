package br.com.alura.techcase.api.controller;

import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.api.dto.user.UserDetail;
import br.com.alura.techcase.api.model.User;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get/{username}")
    public ResponseEntity<UserDetail> getUserByUsername(@PathVariable String username) throws NotFoundException {
        Optional<User> userOptional = userService.getUserByUserName(username);
        var user = userOptional.orElseThrow(() -> new NotFoundException("User with username " + username + " not found."));
        return ResponseEntity.ok(new UserDetail(user));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserForm form) {
        var user = userService.createUser(form);
        var uri = UriComponentsBuilder.newInstance()
                .path("/api/user/create/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
