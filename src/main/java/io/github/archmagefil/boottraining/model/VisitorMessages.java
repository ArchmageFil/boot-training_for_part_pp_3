package io.github.archmagefil.boottraining.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class VisitorMessages {
    private String result;
    private Long id;

    public String getResult() {
        String r = result;
        result = null;
        return r;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getId() {
        long i = id;
        id = null;
        return i;
    }

    public void setId(Long id) {
        this.id = id;
    }
}