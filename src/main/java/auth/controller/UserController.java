package auth.controller;

import auth.entity.UserApp;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public @ResponseBody ResponseEntity registerUser(String username, String password){
        UserApp userApp = new UserApp();
        if (username.equals("") || password.equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect username or password");
        }
        UserApp user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        userApp.setUsername(username);
        userApp.setPassword(password);
        userService.createUser(userApp);
        log.info("LOGIN - "+username);
        return ResponseEntity.status(HttpStatus.OK).body(userApp);
    }
}
