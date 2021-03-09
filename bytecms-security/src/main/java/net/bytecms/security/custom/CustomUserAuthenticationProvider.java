package net.bytecms.security.custom;

import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomUserLoginRiskCheck customUserLoginRiskCheck;

    @Autowired
    MenuService menuService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = null;
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        try {
            user = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            customUserLoginRiskCheck.writeErrorLog(username);
            throw e;
        } catch (CustomException| LockedException e) {
            throw new DisabledException(e.getMessage());
        }
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            customUserLoginRiskCheck.writeErrorLog(username);
            throw new DisabledException("");
        }

//        验证证书
//        try {
//            this.customUserLoginRiskCheck.checkerLicense();
//        } catch (Exception e) {
//            throw new DisabledException(Checker.BeNotNull(e.getCause()) ? e.getCause().getMessage() : e.getMessage());
//        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
