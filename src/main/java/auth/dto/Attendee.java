package auth.dto;

import auth.entity.UserApp;
import lombok.Data;

@Data
public class Attendee {
    private UserApp user;
    private String name;
    private String surname;
    private String email;
    private String info;
}
