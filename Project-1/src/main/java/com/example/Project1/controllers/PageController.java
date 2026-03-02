package com.example.Project1.controllers;


import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController 
{
    
    @GetMapping("/")
    public String index()
    {
        return "index";
    }


    @GetMapping("/login")
    public String login()
    {
        return "login";
    }
    
}
