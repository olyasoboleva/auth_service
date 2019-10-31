package auth.controller;

import auth.entity.UserApp;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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
        log.info("SIGNUP - "+username);

        return createAttendee(userApp.getUserId().toString());
    }

    private @ResponseBody ResponseEntity createAttendee(String uid){
        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/test/kek")
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
}
