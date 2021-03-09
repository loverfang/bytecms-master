package net.bytecms.addons.service;

import net.bytecms.core.annotation.AddonInterceptor;
import net.bytecms.core.annotation.AddonsMapping;

import java.util.Map;

public interface OssService  {

    @AddonsMapping(name = "test")
    @AddonInterceptor
    public void test(Map<String,Object> params);


}
