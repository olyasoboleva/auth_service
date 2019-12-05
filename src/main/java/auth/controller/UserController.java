package auth.controller;

import auth.dto.Attendee;
import auth.dto.Organization;
import auth.entity.UserApp;
import auth.feign.UserClient;
import auth.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserClient userClient;

    @PostMapping(value = "/signup/attendee", produces = "application/json")
    public @ResponseBody ResponseEntity signup(@RequestBody Attendee attendee){
        ResponseEntity responseEntity = registerUser(attendee.getUser());
        UserApp user;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            user = (UserApp) responseEntity.getBody();
            attendee.setUserId(user.getUserId().toString());
            try {
                responseEntity = userClient.saveAttendee(attendee);
            } catch (FeignException ex) {
                userService.deleteUser(user);
                log.info("DELETE - attendee "+user.getUsername());
                return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
            }
        }
        return responseEntity;
    }

    @PostMapping(value = "/signup/organization", produces = "application/json")
    public @ResponseBody ResponseEntity signup(@RequestBody Organization organization){
        ResponseEntity responseEntity = registerUser(organization.getUser());
        UserApp user;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            user = (UserApp) responseEntity.getBody();
            organization.setUserId(user.getUserId().toString());
            try {
                responseEntity = userClient.saveOrganization(organization);
            } catch (FeignException ex) {
                userService.deleteUser(user);
                log.info("DELETE - attendee "+user.getUsername());
                return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
            }
        }
        return responseEntity;
    }

    @ResponseBody private ResponseEntity registerUser(UserApp user){
        UserApp userApp = new UserApp();
        if (user.getUsername().equals("") || user.getPassword().equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect username or password");
        }
        UserApp anotherUser = userService.getUserByUsername(user.getUsername());
        if (anotherUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        userApp.setUsername(user.getUsername());
        userApp.setPassword(user.getPassword());
        userApp.setAttendee(user.isAttendee());
        userService.createUser(userApp);
        log.info("SIGNUP - "+user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(userApp);
    }
}
