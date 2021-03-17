package net.bytecms.security.custom;


import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.utils.Checker;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public class CustomJwtTokenStore extends JwtTokenStore {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    ByteCmsConfig byteCmsConfig;

    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        if (!byteCmsConfig.getAllowMultiLogin().booleanValue() && authentication.getPrincipal() instanceof CustomJwtUser) {
            CustomJwtUser customJwtUser = (CustomJwtUser)authentication.getPrincipal();
            String userId = customJwtUser.getUserId();
            if (Checker.BeNotBlank(userId).booleanValue()) {
                baseRedisService.set("allow_multi_login_key:" + userId, token.getValue(), token.getExpiresIn(), TimeUnit.SECONDS);
            }
        }
        super.storeAccessToken(token, authentication);
    }
}
