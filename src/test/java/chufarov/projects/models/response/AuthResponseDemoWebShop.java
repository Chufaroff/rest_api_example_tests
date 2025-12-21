package chufarov.projects.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDemoWebShop {

    private String token, userId, expires;
}
