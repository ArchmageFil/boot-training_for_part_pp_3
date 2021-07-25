package io.github.archmagefil.boottraining.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
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