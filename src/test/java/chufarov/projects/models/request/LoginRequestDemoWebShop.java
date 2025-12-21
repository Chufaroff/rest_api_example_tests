package chufarov.projects.models.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDemoWebShop {

    private String email;
    private String password;
}
