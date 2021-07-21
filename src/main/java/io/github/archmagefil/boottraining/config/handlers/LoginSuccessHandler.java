package io.github.archmagefil.boottraining.config.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        Set<String> authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        if (authorities.contains("ROLE_admin")) {
            httpServletResponse.sendRedirect("/admin/");
        } else {
            httpServletResponse.sendRedirect("/user");
        }
    }
}