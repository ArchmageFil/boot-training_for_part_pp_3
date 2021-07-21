package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LandingController {
    private final UserService service;

    @Autowired
    public LandingController(UserService service) {
        this.service = service;
    }

    /**
     * @return Страничка по умолчанию, приветствие, ссылки
     */
    @GetMapping("/")
    public String landing() {
        return "index.html";
    }

    /**
     * @return Страница с формой авторизациии.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @GetMapping(value = "/login", params = "error")
    public String loginErrorPage(Model model) {
        model.addAttribute("bad_credentials", "true");
        return "login.html";
    }

    /**
     * @param principal Текущий пользователь
     * @param model     Модель для th
     * @return Страничка зарегистрированного пользователя.
     */
    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        model.addAttribute("user", service.findByUsername(principal.getName()));
        return "user.html";
    }
}