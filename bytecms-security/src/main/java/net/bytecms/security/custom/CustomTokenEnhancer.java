package net.bytecms.security.custom;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        CustomJwtUser user = (CustomJwtUser)oAuth2Authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("userId", user.getUserId());
        additionalInfo.put("deptId", user.getDeptId());
        additionalInfo.put("user_name", user.getUserName());
        additionalInfo.put("name", user.getName());
        additionalInfo.put("role_signs", user.getRoleSigns());
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}
