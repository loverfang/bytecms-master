package net.bytecms.security.custom;


import net.bytecms.core.utils.BaseContextKit;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import net.bytecms.system.api.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    MenuService menuService;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        Set<String> authPerms = menuService.selectPermsByUid(BaseContextKit.getUserId());
        Set<String> needAuthSets = (Set<String>)configAttributes.stream().map(auth -> auth.getAttribute()).collect(Collectors.toSet());
        boolean beMatch = authPerms.containsAll(needAuthSets);
        if (!beMatch){
            throw new AccessDeniedException("");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

