package auth.dto;

import auth.entity.UserApp;
import lombok.Data;

@Data
public class Organization {
    private UserApp user;
    private String name;
    private String phone;
    private String email;
    private String info;
}
