package net.bytecms.security.service;

import java.util.ArrayList;
import java.util.Collection;

import net.bytecms.security.custom.CustomJwtUser;
import net.bytecms.system.api.system.MenuService;
import net.bytecms.system.api.system.UserService;
import net.bytecms.system.dto.system.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    CustomUserLoginRiskCheck customUserLoginRiskCheck;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDto userDto = customUserLoginRiskCheck.loginRiskCheck(userName);
        if (userDto == null) {
            throw new UsernameNotFoundException("");
        }
        if (userDto.getStatus().intValue() == 1) {
            throw new LockedException("");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        CustomJwtUser customJwtUser = new CustomJwtUser(userDto.getUsername(), userDto.getPassword(), grantedAuthorities);
        customJwtUser
            .setUserName(userDto.getUsername()).setName(userDto.getName())
            .setUserId(userDto.getId())
            .setDeptId(userDto.getOrgId())
            .setRoleSigns(userDto.getRoleSigns());
        return customJwtUser;
    }
}

