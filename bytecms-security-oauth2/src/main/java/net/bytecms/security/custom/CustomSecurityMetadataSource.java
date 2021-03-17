package net.bytecms.security.custom;

/**
 * @Author Luo.z.x
 * @Date 2021/3/4 16:19
 * @Version 1.0
 */
import com.google.common.collect.Sets;
import net.bytecms.core.utils.Checker;

import java.util.Collection;
import java.util.Set;

import net.bytecms.security.properties.PermitAllUrlProperties;
import net.bytecms.system.api.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    MenuService menuService;

    @Autowired
    PermitAllUrlProperties permitAllUrlProperties;

    @Value("${spring.application.name}")
    public String application;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url = ((FilterInvocation)object).getHttpRequest().getServletPath();
        Collection<ConfigAttribute> atts = Sets.newHashSet();
        boolean authc = permitAllUrlProperties.ckTheSameApi(application, url);
        if (authc) {
            atts.add(new SecurityConfig("NOT_REQUIRED_HAVE_PERM"));
            return atts;
        }
        Set<String> perms = menuService.selectPermsByUrl(url);
        if (Checker.BeEmpty(perms).booleanValue()) {
            SecurityConfig securityConfig = new SecurityConfig("NOT_REQUIRED_HAVE_PERM");
            atts.add(securityConfig);
        } else {
            perms.stream().forEach(perm -> {
                if (Checker.BeNotBlank(perm).booleanValue()) {
                    SecurityConfig securityConfig = new SecurityConfig(perm);
                    atts.add(securityConfig);
                }
            });
        }
        return atts;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}