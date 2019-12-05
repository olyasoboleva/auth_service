package auth.dto;

import auth.entity.UserApp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Attendee {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserApp user;
    private String name;
    private String surname;
    private String email;
    private String skills;
    private String userId;
}
