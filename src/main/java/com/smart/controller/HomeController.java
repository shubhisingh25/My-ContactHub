package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
            Model model,
            HttpSession session) {

        try {
            if (!agreement) {
                session.setAttribute("message", new Message("Please accept the terms and conditions!", "alert-danger"));
                return "signup";
            }

            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "signup";
            }

            // Check if email exists
            if (userRepository.findByEmail(user.getEmail()) != null) {
                session.setAttribute("message", new Message("Email already registered!", "alert-danger"));
                model.addAttribute("user", user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            
            
			user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Registration successful!", "alert-success"));
            
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }
      
    }
  //handler for custom login
  	@GetMapping("/signin")
  	public String customLogin(Model model)
  	{
  		model.addAttribute("title","Login Page");
  		return "login.html";
  	}

}