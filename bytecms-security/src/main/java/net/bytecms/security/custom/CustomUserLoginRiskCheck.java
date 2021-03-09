package net.bytecms.security.custom;

import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import java.util.Set;

import net.bytecms.system.api.system.UserService;
import net.bytecms.system.dto.system.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomUserLoginRiskCheck {
    @Autowired
    UserService userService;
    @Autowired
    BaseRedisService baseRedisService;
    @Autowired
    ThinkCmsConfig cmsConfig;

    public UserDto loginRiskCheck(String userName) throws CustomException {
        checkMaxError(userName);
        UserDto userDto = null;
        if (Checker.BeNotBlank(userName).booleanValue()) {
            userDto = userService.findUserByUsername(userName);
            if (Checker.BeNotNull(userDto)) {
                Set<String> roleSigns = userService.selectRoleSignByUserId(userDto.getId());
                if (Checker.BeNotEmpty(roleSigns).booleanValue()) {
                    userDto.setRoleSigns(roleSigns);
                }
            }
        }
        return userDto;
    }

    public void writeErrorLog(String userName) {
        String key = "error_input_pass_times_" + userName;
        long expireTime = this.baseRedisService.getExpire(key);
        if (expireTime == -1L) {
            baseRedisService.increment(key, 1, 600L);
        } else {
            baseRedisService.increment(key, 1);
        }
    }

    private void checkMaxError(String userName) throws CustomException {
        String key = "error_input_pass_times_" + userName;
        if (baseRedisService.hasKey(key).booleanValue()) {
            Integer times = (Integer)baseRedisService.get(key);
            if (times.intValue() >= 6) {
                long expireTime = baseRedisService.getExpire(key);
                if (expireTime == -1L) {
                    baseRedisService.setExpireTime("error_input_pass_times_" + userName, Long.valueOf(300L));
                }
                throw new CustomException(ApiResult.result(7001));
            }
        }
    }

}
