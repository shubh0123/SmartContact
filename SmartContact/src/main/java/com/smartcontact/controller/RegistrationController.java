package com.smartcontact.controller;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
//    handler for registering user
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user")User user,BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false")boolean agreement, Model model, HttpSession session){
    try{
        if(!agreement){
            throw new Exception("You have not agreed with the terms and condition");//error handling
        }
        System.out.println("ERROR "+ result1.toString());
        //validation
        if(result1.hasErrors()){
            System.out.println("ERROR "+ result1.toString());
            model.addAttribute("user",user);
            return "signup";
        }
        long checkEmail=userRepository.countByEmail(user.getEmail());
        System.out.println(checkEmail);
        if(checkEmail==1){

            throw new Exception("Email-Id is already registered !!");
        }

        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("user", new User());
        session.setAttribute("message", new Message("Successfully Registered!!!", "alert-success"));

        System.out.println("Agreement:" +agreement);
        System.out.println(user);
    }
    catch(Exception e){
        e.printStackTrace();
        model.addAttribute("user",user);
        session.setAttribute("message", new Message("Something went wrong !!!"+e.getMessage(),"alert-danger"));
        return "signup";
    }
        return "signup";
    }
}
