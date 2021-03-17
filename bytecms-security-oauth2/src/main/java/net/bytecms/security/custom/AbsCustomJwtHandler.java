package net.bytecms.security.custom;

/**
 * @Author Luo.z.x
 * @Date 2021/3/4 16:13
 * @Version 1.0
 */
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.BaseContextKit;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public abstract class AbsCustomJwtHandler {

    @Autowired
    BaseRedisService<String, Object> baseRedisService;

    @Autowired
    ByteCmsConfig byteCmsConfig;

    public abstract void handlerJwtToken(Authentication paramAuthentication, JwtTokenStore paramJwtTokenStore) throws CustomException;

    protected void handAppUserSession(Map<String, Object> details, String tokenValue) {
        handAppToken(tokenValue);
        String openId = Checker.BeNull(details.get("openId")) ? null : details.get("openId").toString();
        String userId = Checker.BeNull(details.get("userId")) ? null : details.get("userId").toString();
        ckparameters(new String[] { openId, userId });
        BaseContextKit.setOpenId(openId);
        BaseContextKit.setUserId(userId);
    }

    protected void handPlatUserSession(Map<String, Object> details, String tokenValue) {
        String uid = Checker.BeNull(details.get("userId")) ? null : details.get("userId").toString();
        String deptId = Checker.BeNull(details.get("deptId")) ? null : details.get("deptId").toString();
        String userName = Checker.BeNull(details.get("user_name")) ? null : details.get("user_name").toString();
        String name = Checker.BeNull(details.get("name")) ? null : details.get("name").toString();
        Collection<String> roleSigns = Checker.BeNull(details.get("role_signs")) ? new HashSet<>() : (Collection<String>)details.get("role_signs");
        ckparameters(new String[] { uid });
        if (!byteCmsConfig.getAllowMultiLogin().booleanValue()) {
            checkAllowMulitLogin(uid, tokenValue);
        }
        BaseContextKit.setUserId(uid);
        BaseContextKit.setUserName(userName);
        BaseContextKit.setDeptId(deptId);
        BaseContextKit.setName(name);
        BaseContextKit.setRoleSigns(roleSigns);
    }

    private void ckparameters(String... params) {
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (Checker.BeBlank(params[i]).booleanValue()) {
                    throw new InvalidTokenException("Invalid jwt token!");
                }
            }
        }
    }

    private void checkAllowMulitLogin(String userId, String tokenValue) {
        if (Checker.BeNotBlank(userId).booleanValue() && Checker.BeNotBlank(tokenValue).booleanValue()) {
            Object redisTokenValue = baseRedisService.get("allow_multi_login_key:" + userId);
            if (Checker.BeNotNull(redisTokenValue)) {
                boolean singleLogin = tokenValue.equals(redisTokenValue.toString());
                if (!singleLogin) {
                    throw new AccessDeniedException("");
                }
            }
        }
    }

    private void handAppToken(String tokenValue) {
        Boolean hasKey = baseRedisService.hasKey(tokenValue);
        if (!hasKey.booleanValue()) {
            throw new CustomException(ApiResult.result(7000));
        }
        baseRedisService.setSetExpireTime(tokenValue, SecurityConstants.expireTime);
    }
}

