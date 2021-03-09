package net.bytecms.web.aspect;

import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.Checker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
class CacheClearAspect {

    @Autowired
    BaseRedisService baseRedisService;

    @AfterReturning("execution(* net.bytecms..*.*(..)) && @annotation(net.bytecms.core.annotation.CacheClear)")
    public void process(JoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String tarclas = target.getClass().toString();
        //获取切入方法的数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheClear cacheClear = method.getAnnotation(CacheClear.class);
        String value = cacheClear.value();
        Class<?>[] clzs = cacheClear.clzs();
        String key = cacheClear.key();
        String[] keys = cacheClear.keys();
        String keyPrefix = value + tarclas;
        if (Checker.BeNotBlank(key) && clzs.length==0) {
            clearRedisByKey(keyPrefix + Constants.DOT + key);
        }
        if (Checker.BeNotEmpty(keys) && clzs.length==0) {
            for (String k : keys) {
                clearRedisByKey(keyPrefix + Constants.DOT + k);
            }
        }
        if (Checker.BeNotEmpty(clzs)) {// 清楚整个类的缓存
            List<String> needClearkeys = getKeys(clzs, key, keys);
            if(Checker.BeNotEmpty(needClearkeys)){
				for (String clearKey : needClearkeys) {
					clearRedisByKey(value+clearKey);
				}
			}else{
                for(Class<?> clz:clzs){
                    clearRedisByKey(value+clz.toString()+Constants.DOT);
                }
            }
        }
    }

    private List<String> getKeys(Class<?>[] clzs, String key, String[] keys) {
        List<String> needClearkeys = new ArrayList<>(16);
        if (clzs != null && clzs.length > 0) {
            List<String> keyList = new ArrayList<>(16);
            if (keys != null && keys.length > 0) {
                keyList.addAll(Arrays.asList(keys));
            }
            if (Checker.BeNotBlank(key)) {
                keyList.add(key);
            }
            for (Class<?> clz : clzs) {
                Method[] methods = clz.getMethods();
                if (methods != null && methods.length > 0) {
                    for (Method method : methods) {
                        boolean containMethods = keyList.contains(method.getName());
                        if (containMethods) {
                        	String getKey=clz.toString() + Constants.DOT + method.getName();
                        	if(!needClearkeys.contains(getKey)){
								needClearkeys.add(getKey);
							}
                        }
                    }
                }
            }
        }
        return needClearkeys;
    }

    private void clearRedisByKey(String key) {
        if (Checker.BeNotBlank(key)) {
            key += Constants.delRedisKey;
            baseRedisService.removeBlear(key);
        }
    }
}
