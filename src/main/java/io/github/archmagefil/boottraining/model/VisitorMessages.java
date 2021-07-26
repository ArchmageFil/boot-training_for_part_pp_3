package io.github.archmagefil.boottraining.model;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VisitorMessages {
    private String result;

    public String getResult() {
        String r = result;
        result = null;
        return r;
    }

    public void setResult(String result) {
        this.result = result;
    }
}