package io.github.archmagefil.boottraining.controller;

import io.github.archmagefil.boottraining.model.Role;
import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.model.VisitorMessages;
import io.github.archmagefil.boottraining.service.RoleService;
import io.github.archmagefil.boottraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class AdminPanelController {
    private final UserService userService;
    private final RoleService roleService;
    private VisitorMessages messages;

    @Autowired
    public AdminPanelController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute("roles")
    public List<Role> roleList() {
        return roleService.getAllRoles();
    }

    /**
     * Основной список пользователей с формой нового пользователя
     */
    @GetMapping("/")
    public String listUsers(@RequestParam(value = "r", defaultValue = "false")
                                    Boolean isRedirect, Model model) {
        model.addAttribute("userDto", new UserDto());
        // Если в запросе пришла инфа о наличии доп. сообщениий - добавить в модель
        if (isRedirect) {
            model.addAttribute("result", messages.getResult());
        }
        model.addAttribute("userList", userService.getAllUsers());
        return "/index.html";
    }

    /**
     * Создаание нового пользователя
     *
     * @param tempUser ДТОшка для подготовки к созданию сущности
     * @return возвращает на основную админку.
     */
    @PostMapping("/")
    public String addUser(@ModelAttribute UserDto tempUser) {
        messages.setResult(userService.addUser(tempUser));
        return "redirect:/?r=true";
    }

    /**
     * Обработка формы на редактирование пользователя.
     */
    @PatchMapping("/")
    public String updateUser(@ModelAttribute User tempUser) {
        // Ставим ранее сохраненный ИД
        tempUser.setId(messages.getId());
        if (tempUser.getId() == null) {
            messages.setResult("Ошибка запроса, попробуй еще раз.");
            return "redirect:/admin/?r=true";
        }
        // Кидаем в сообщения результат операции и возвращаемся на основную страницу
        messages.setResult(userService.updateUser(tempUser));
        return "redirect:/?r=true";
    }

    /**
     * Обработка запроса на удаление из БД пользователя по ИД
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable long id) {
        messages.setResult(userService.deleteUser(id));
        return "redirect:/?r=true";
    }

    /**
     * Очистка БД
     */
    @DeleteMapping("/db_gen/")
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

    @GetMapping(value = "/login", params = "error")
    public String loginErrorPage(Model model) {
        model.addAttribute("bad_credentials", "true");
        return "login.html";
    }

    @Autowired
    public void setMessages(VisitorMessages messages) {
        this.messages = messages;
    }
}