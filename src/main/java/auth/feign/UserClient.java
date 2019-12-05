package auth.feign;

import auth.dto.Attendee;
import auth.dto.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", url = "http://localhost:8080")
public interface UserClient {

    @PostMapping("/chat/save/attendee")
    public ResponseEntity saveAttendee(@RequestBody Attendee attendee);

    @PostMapping("/event/save/organization")
    public ResponseEntity saveOrganization(@RequestBody Organization organization);
}
