package auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("auth.entity")
@EnableJpaRepositories("auth.repository")
//@EnableEurekaClient
public class AuthApplication {
    public static void main(String[] args){
        SpringApplication.run(AuthApplication.class, args);
    }
}
