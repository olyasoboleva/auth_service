package auth.controller;

import auth.dto.Attendee;
import auth.dto.Organization;
import auth.entity.UserApp;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup/attendee", produces = "application/json")
    public @ResponseBody ResponseEntity signup(@RequestBody Attendee attendee){
        ResponseEntity responseEntity = registerUser(attendee.getUser());
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            attendee.setUser((UserApp) responseEntity.getBody());
            responseEntity = createAttendee(attendee);
        }
        return responseEntity;
    }

    @PostMapping(value = "/signup/organization", produces = "application/json")
    public @ResponseBody ResponseEntity signup(@RequestBody Organization organization){
        ResponseEntity responseEntity = registerUser(organization.getUser());
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            organization.setUser((UserApp) responseEntity.getBody());
            responseEntity = createOrganization(organization);
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
        userService.createUser(userApp);
        log.info("SIGNUP - "+user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(userApp);
    }


    private @ResponseBody ResponseEntity createAttendee(Attendee attendee){
        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("userId", attendee.getUser().getUserId().toString())
                .add("name", attendee.getName())
                .add("surname", attendee.getSurname())
                .add("email", attendee.getEmail())
                .add("skills", attendee.getInfo())
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/chat/add/attendee")
                .post(formBody)
                .build();
        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            return ResponseEntity.status(response.code()).body(response.body().string());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create attendee");
        }
    }

    private @ResponseBody ResponseEntity createOrganization(Organization organization){
        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("userId", organization.getUser().getUserId().toString())
                .add("name", organization.getName())
                .add("phone", organization.getPhone())
                .add("email", organization.getEmail())
                .add("info", organization.getDetails())
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/event/add/org")
                .post(formBody)
                .build();
        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            return ResponseEntity.status(response.code()).body(response.body().string());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create organization");
        }
    }
}
