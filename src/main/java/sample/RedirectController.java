package sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedirectController {

	@GetMapping("/redirect")
	String redirect() {
		return "redirect:/login";
	}

	@ResponseBody
	@GetMapping("/login")
	String login() {
		return "login";
	}
}
