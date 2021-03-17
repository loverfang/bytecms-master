package net.bytecms.security.properties;

import net.bytecms.core.utils.Checker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "permission")
public class PermitAllUrlProperties {

    public List<Application> application = new ArrayList<>();
    public List<Application> getApplication() {
        return this.application;
    }

    public boolean ckTheSameApi(String name, String apiUrl) {
        if (Checker.BeBlank(name).booleanValue()){
            return false;
        }
        boolean res = false;
        for (Application app : getApplication()) {
            if (name.equals(app.getAppName())) {
                for (String api : app.getAuthc()) {
                    if (apiUrl.equals(api)) {
                        res = true;
                        break;
                    }
                }
            }
        }
        return res;
    }

    public List<String> ignores() {
        List<String> ignores = new ArrayList<>(16);
        if (Checker.BeNotEmpty(application).booleanValue()) {
            application.forEach(app -> ignores.addAll(app.getIgnore()));
        }
        return ignores;
    }

}
