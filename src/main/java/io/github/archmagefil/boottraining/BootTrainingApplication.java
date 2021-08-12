package io.github.archmagefil.boottraining;

import io.github.archmagefil.boottraining.util.RoleFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BootTrainingApplication {

    @SuppressWarnings("SpellCheckingInspection")
    public static void main(String[] args) {
        SpringApplication.run(BootTrainingApplication.class, args);
        String url = "http://127.0.0.1:8080";
        String os = System.getProperty("os.name").toLowerCase(); // получаем название операционной системы
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.contains("win")) {
                // не поддерживаются ссылки формата "leodev.html#someTag"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url); // если windows, открываем урлу через командную строку
            } else if (os.contains("mac")) {
                rt.exec("open " + url); // аналогично в MAC
            } else if (os.contains("nix") || os.contains("nux")) {
                // c nix системами все несколько проблематичнее
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx"};
                // Формируем строку с вызовом всем браузеров через логическое ИЛИ в shell консоли
                // "browser0 "URI" || browser1 "URI" ||..."
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (Exception e) {
            // игнорируем все ошибки
        }
    }

    @Configuration
    public static class FormatterAdder implements WebMvcConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new RoleFormatter());
        }
    }
}
