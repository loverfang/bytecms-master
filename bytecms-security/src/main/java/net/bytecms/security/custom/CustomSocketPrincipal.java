package net.bytecms.security.custom;

import lombok.Data;
import java.security.Principal;

@Data
public class CustomSocketPrincipal implements Principal {
    private String userName;
    private String id;
    public CustomSocketPrincipal(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    @Override
    public String getName() {
        return getId();
    }
}
