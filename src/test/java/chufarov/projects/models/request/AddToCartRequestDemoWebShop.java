package chufarov.projects.models.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddToCartRequestDemoWebShop {

    private String productId, quantity;
}
