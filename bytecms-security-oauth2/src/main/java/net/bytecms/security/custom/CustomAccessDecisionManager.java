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

/**
 * AccessDecisionManager完成授权的功能。
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    MenuService menuService;

    /**
     * 对一次访问授权，需要传入三个信息。
     * 从票据中可以获取认证用户所拥有的权限，再对比访问资源要求的权限，即可断定当前认证用户是否能够访问该资源。
     *
     * @param authentication 认证过的票据Authentication，确定了谁正在访问资源。
     * @param object 被访问的资源object。
     * @param configAttributes 访问资源要求的权限配置ConfigAttributeDefinition。
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication,
           Object object,
           Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
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

