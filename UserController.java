package com.CodingDojo.LognadReg.Controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CodingDojo.LognadReg.Models.User;
import com.CodingDojo.LognadReg.Service.UserService;
import com.CodingDojo.LognadReg.Validator.UserValidator;

@Controller
public class UserController {
	
	private final UserService userService;
	private final UserValidator userValidator;
    
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

	@GetMapping("")
	public String home(@ModelAttribute("user") User user) {
		return "/LognadReg/index.jsp";
	}
	
	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		userValidator.validate(user, result);
		if(result.hasErrors()) {
			return "/LognadReg/index.jsp";
		}
//		this.userService.registerUser(user);
		User u = this.userService.registerUser(user);
		session.setAttribute("userid", u.getId());
		return "redirect:/dashboard";
	}
	
	@GetMapping("/dashboard")
	public String dash(Model model, HttpSession session) {
		Long id = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(id);
		model.addAttribute("loggedinuser", loggedinuser);
		return "/LognadReg/dashboard.jsp";
	}
	
	@GetMapping("/logout")
	public String bye(HttpSession session) {
		session.invalidate();
		return "redirect:";
	}
	
	//RequestParam needed when you use regular form
	@PostMapping("/login")
	public String login(@RequestParam("email")String email, @RequestParam("password")String password, HttpSession session, RedirectAttributes redirectAttributes) {
		Boolean isLegit = this.userService.authenticateUser(email, password);
		if(isLegit) {
			User user = this.userService.findByEmail(email);
			session.setAttribute("userid", user.getId());
			return "redirect:/dashboard";
		} 
		redirectAttributes.addFlashAttribute("error", "Your login input does not match anything stored in our database. Try logging in again. ");
		return "redirect:";
	}
	
}
	
