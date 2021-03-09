package net.bytecms.security.custom;

import net.bytecms.core.enumerate.UserForm;
import net.bytecms.core.handler.CustomException;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtHandler extends AbsCustomJwtHandler {
    @Override
    public void handlerJwtToken(Authentication authentication, JwtTokenStore tokenStore) throws CustomException {
        String tokenValue = authentication.getPrincipal().toString();
        OAuth2Authentication oauth = tokenStore.readAuthentication(tokenValue);
        Map<String, Object> details = (Map<String, Object>)oauth.getDetails();
        boolean isAppUser = (details.containsKey(UserForm.USER_FORM.getCode()) && UserForm.APP_USER.getCode().equals(details.get(UserForm.USER_FORM.getCode())));
        if (isAppUser) {
            handAppUserSession(details, tokenValue);
        } else {
            handPlatUserSession(details, tokenValue);
        }
    }
}
