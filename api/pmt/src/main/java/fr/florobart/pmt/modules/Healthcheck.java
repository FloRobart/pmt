package fr.florobart.pmt.modules;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class Healthcheck {
    @GetMapping
    public static String getHealthcheck() {
        return "OK";
    }
}
