package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.model.Role;
import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.VisitorMessages;
import io.github.archmagefil.boottraining.service.RoleService;
import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                                    Boolean isRedirect, Model model) {
        model.addAttribute("authenticatedUser", SecurityContextHolder
                .getContext().getAuthentication().getPrincipal());
        model.addAttribute("userDto", new UnverifiedUser());
        model.addAttribute("AllUsersList", userService.getAllUsers());
        return "/index.html";
    }
//
//    /**
//     * Создание нового пользователя
//     *
//     * @param tempUser ДТОшка для подготовки к созданию сущности
//     * @return возвращает на основную админку.
//     */
//    @PostMapping("/")
//    @Secured({"ROLE_ADMIN"})
//    public String addUser(@ModelAttribute("UserDto") UnverifiedUser tempUser) {
//        userService.addUser(tempUser);
//        return "redirect:/?r=true";
//    }
//
//    /**
//     * Обработка формы на редактирование пользователя.
//     */
//    @PatchMapping("/")
//    @Secured({"ROLE_ADMIN"})
//    public String updateUser(@ModelAttribute UnverifiedUser tempUser) {
//        if (tempUser.getId() == null) {
//            messages.setResult("Ошибка запроса, попробуй еще раз.");
//            return "redirect:/admin/?r=true";
//        }
//        userService.updateUser(tempUser);
//        return "redirect:/?r=true";
//    }
//
//    /**
//     * Обработка запроса на удаление из БД пользователя по ИД
//     */
//    @DeleteMapping("/{id}")
//    @Secured({"ROLE_ADMIN"})
//    public String deleteUser(@PathVariable long id) {
//        messages.setResult(userService.deleteUser(id));
//        return "redirect:/?r=true";
//    }

    /**
     * Очистка БД
     */
    @DeleteMapping("/db_gen/")
    @Secured({"ROLE_ADMIN"})
    public String clear(UserService service) {
        messages.setResult(service.clearDB());
        return "redirect:/?r=true";
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