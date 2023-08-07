package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Value("${server.port}")
    private int port;
    @GetMapping(value = "getPort")
    public int getPort() {
        return port;
    }

    //запуск java c другим профилем (после сборки, используя maven)
    // C:\SkyProJava\Hogwarts> C:\Users\Lenovo\.jdks\corretto-17.0.7\bin\java.exe -jar target\school-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=test

}
