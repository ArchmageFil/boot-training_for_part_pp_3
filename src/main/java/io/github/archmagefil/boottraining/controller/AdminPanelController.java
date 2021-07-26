package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.model.Role;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.model.VisitorMessages;
import io.github.archmagefil.boottraining.service.RoleService;
import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminPanelController {
    private final UserService userService;
    private final RoleService roleService;
    private VisitorMessages messages;

    @Autowired
    public AdminPanelController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute("allRolesList")
    public List<Role> allRolesList() {
        return roleService.getAllRoles();
    }

    /**
     * @param isRedirect если перенаправление с ответом, опционально
     * @return Основную страницу админки
     */
    @GetMapping("/")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public String listUsers(@RequestParam(value = "r", defaultValue = "false")
                                    Boolean isRedirect, Model model, Principal principal) {
        model.addAttribute("authenticatedUser", SecurityContextHolder
                .getContext().getAuthentication().getPrincipal());
        model.addAttribute("userDto", new UserDto());
        // Если в запросе пришла инфа о наличии доп. сообщениий - добавить в модель
        if (isRedirect) {
            model.addAttribute("result", messages.getResult());
        }
        model.addAttribute("AllUsersList", userService.getAllUsers());
        return "/index.html";
    }

    /**
     * @return Страница с формой авторизациии.
     */

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    /**
     * Вместо обработчика
     */
    @RequestMapping("/login/good")
    public String goodLogin() {
        return "redirect:/";
    }

    @Autowired
    public void setMessages(VisitorMessages messages) {
        this.messages = messages;
    }
}