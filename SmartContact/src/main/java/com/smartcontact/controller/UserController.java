package com.smartcontact.controller;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;



    @ModelAttribute
    public void addCommonContent(Model model, Principal principal){
        String username=principal.getName();
        System.out.println(username);
        User user=userRepository.getUserByEmail(username);//Here we are returnning the credentials of user after login
        System.out.println(user);
        model.addAttribute("user", user);
    }
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal)//Object of principal stores the unique identifier of perticular login credentials;
    {

        return "normal/user_dashboard";
    }
    //open add contact handler
    @GetMapping("/addContact")
    public String addContact(Model model){
        model.addAttribute("title","Add Contact-smartContact Manager" );
        model.addAttribute("contact", new Contact());
        return "normal/addContact";
    }
    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute  Contact contact, @RequestParam("profileImage") MultipartFile file, 
    		Principal principal,HttpSession session) {
//        storing profile image--processing and uploading
        try {
            if (file.isEmpty()) {
                System.out.println("No file is received");
            } else {

                contact.setImgName(file.getOriginalFilename());
                contact.setType(file.getContentType());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path fullPath = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                contact.setImagePath(String.valueOf(fullPath));

                Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
                System.gc();
            }


            String username = principal.getName();
            User user = userRepository.getUserByEmail(username);
            contact.setUser(user);
            user.getContacts().add(contact);
            userRepository.save(user);
            session.setAttribute("message", new Message("Contact is Successfuly Added !!!" ,"alert-success"));

        }
        catch (Exception e){
            System.out.println("Error "+e.getMessage());
            session.setAttribute("message", new Message("Something went wrong !!!"+e.getMessage(),"alert-danger"));
            e.printStackTrace();
        }
        return "normal/addContact";
        }
    }


