package auth.dto;

import auth.entity.UserApp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Organization {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserApp user;
    private String name;
    private String phone;
    private String email;
    private String info;
    private String userId;
}
