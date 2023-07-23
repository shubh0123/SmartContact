package com.smartcontact.controller;

import com.smartcontact.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomepageController {

  @RequestMapping("/")
    public String home(Model model){
      model.addAttribute("title","Home -smartContact Manager");
    return "home";
  }
  @RequestMapping("/about")
  public String about(Model model){
    model.addAttribute("title","About -smartContact Manager");
    return "about";
  }
  @RequestMapping("/signup")
  public String signup(Model model){
    model.addAttribute("title","SignUp -smartContact Manager");
    model.addAttribute("user",new User());
    return "signup";
  }
//handler for custom login
  @GetMapping("/login")
public String CustomLogin(Model model){
    model.addAttribute("title","SignIn -smartContact Manager");

    return "normal/login";
  }

}

