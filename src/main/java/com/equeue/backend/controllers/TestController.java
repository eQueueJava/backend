package com.equeue.backend.controllers;

import com.equeue.backend.models.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/test")
public class TestController {

    @GetMapping
    public String test(){
        return "QQQQQQQQ";
    }

    @GetMapping("/1")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin(){
        return "ADMIN";
    }

    @GetMapping("/2")
    @PreAuthorize("hasAnyAuthority('COSTUMER', 'ADMIN')")
    public String costumer(){
        return "COSTUMER, ADMIN";
    }

    @GetMapping("/3")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'COSTUMER', 'ADMIN')")
    public String client(){
        return "'CLIENT', 'COSTUMER', 'ADMIN'";
    }
}
