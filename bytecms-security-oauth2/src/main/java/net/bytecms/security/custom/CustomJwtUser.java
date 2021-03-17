package net.bytecms.security.custom;

import java.util.Collection;
import java.util.Set;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 用于认证的用户信息
 */
@Data
@Accessors(chain = true)
public class CustomJwtUser extends User {
    private String userId;
    private String deptId;
    private String userName;
    private String name;
    private Set<String> roleSigns;

    public CustomJwtUser(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
