package chufarov.projects.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDemoQa {

    private String userId;
    private String username;
    private String password;
    private String token;
    private String expires;
    private String created_date;
    private Boolean isActive;
}
