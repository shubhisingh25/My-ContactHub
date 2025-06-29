package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("USERNAME: " + userEmail);
        User user = userRepository.findByEmail(userEmail);
        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        Message message = (Message) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "normal/add_contact_form";
    }

    @Transactional
    @PostMapping("/process-contact")
    public String processContact(
            @ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal,
            Model model,
            HttpSession session) {

        try {
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            // File upload logic
            if (file.isEmpty()) {
                System.out.println("File is empty");
                contact.setImage("contact.jpg");
            } else {
                String fileName = file.getOriginalFilename();
                contact.setImage(fileName);

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);

            this.userRepository.save(user);

            System.out.println("DATA: " + contact);
            System.out.println("Added to the database");

            session.setAttribute("message",
                    new Message("Your contact is added! Add more...", "success"));

        } catch (IOException e) {
            e.printStackTrace();
            session.setAttribute("message",
                    new Message("Something went wrong! Try again...", "danger"));
        }

        return "redirect:/user/add-contact";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(
            Model model,
            @PathVariable("page") Integer page,
            Principal principal) {

        model.addAttribute("title", "Show User Contacts");
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    // Showing particular contact details
    @RequestMapping("/{cId}/contact")
    public String showContactDetail(
            @PathVariable("cId") Integer cId,
            Model model,
            Principal principal) {

        System.out.println("CID = " + cId);

        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        if (contactOptional.isEmpty()) {
            model.addAttribute("title", "Contact Not Found");
            return "normal/contact_detail";
        }

        Contact contact = contactOptional.get();
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if (contact.getUser() != null && user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        } else {
            model.addAttribute("contact", null);
        }
      return "normal/contact_detail";
    }
  //delete contact handler
    @Transactional
    @GetMapping("/delete/{cid}")
    public String deleteContact(
            @PathVariable("cid") Integer cId,
            Model model,
            HttpSession session,
            Principal principal) {

        // Load the contact
        Contact contact = this.contactRepository.findById(cId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Get current user
        User user = this.userRepository.getUserByUserName(principal.getName());

        // Security check: prevent deleting someone else’s contact
        if (contact.getUser() == null || contact.getUser().getId() != user.getId()) {

            session.setAttribute("message", new Message("You are not authorized to delete this contact.", "danger"));
            return "redirect:/user/show-contacts/0";
        }

        // Actually delete
        this.contactRepository.delete(contact);

        session.setAttribute("message", new Message("Contact deleted successfully.", "success"));

        return "redirect:/user/show-contacts/0";
    } 
  //Open update form handler
  	@PostMapping("/update-contact/{cid}")
  	public String UpdateForm(@PathVariable("cid") Integer cid,
  			Model model)
  	{
  		model.addAttribute("title", "Update Contact");
  		
  		this.contactRepository.findById(cid);
  		
  		Contact contact=this.contactRepository.findById(cid).get();
  		
  		model.addAttribute("contact",contact);
  		
  		return"normal/update_form";
  	}   
  //update contact handler
  	@RequestMapping(value="/process-update",method= RequestMethod.POST)
  	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,Principal principle)
  	{
  		
  		try {
  			
  			//fetch old contact details
  			Contact oldcontactDetails=this.contactRepository.findById(contact.getcId()).get();
  			
  			
  			if(!file.isEmpty())
  			{
  				//delete file
  				File deleteFile=new ClassPathResource("static/img").getFile();
  				File file1=new File(deleteFile,oldcontactDetails.getImage());
  				file1.delete();
  				
  				//update image
  				File saveFile=new ClassPathResource("static/img").getFile();
  				
  				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
  				
  				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
  				contact.setImage(file.getOriginalFilename());
  			}
  			else
  			{
  				contact.setImage(oldcontactDetails.getImage());
  			}
  			
  			User user=this.userRepository.getUserByUserName(principle.getName());
  			
  			contact.setUser(user);
  			
  			this.contactRepository.save(contact);
  			
  			session.setAttribute("message", new Message("Your contact is updated...","success"));
  			
  		}catch(Exception e)
  		{
  			e.printStackTrace();
  		}
  		
  		
  		System.out.println("Conatct NAME="+contact.getName());
  		System.out.println("Contact ID="+contact.getcId());
  		return "redirect:/user/"+contact.getcId()+"/contact";
  	}
  	@GetMapping("/profile")
  	public String yourProfile(Model model, Principal principal) {
  	    String username = principal.getName();
  	    User user = userRepository.getUserByUserName(username);
  	    model.addAttribute("user", user);
  	    model.addAttribute("title", "Profile Page");
  	    return "normal/profile";
  	}
  
}

