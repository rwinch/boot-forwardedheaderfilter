package sample;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedirectController {

	@RequestMapping("/redirect/**")
	String redirect(@RequestParam(defaultValue = "/login") String l) {
		return "redirect:" + l;
	}

	@RequestMapping("/location/**")
	void location(@RequestParam(defaultValue = "/login") String l, HttpServletResponse response) {
		response.setHeader("Location", l);
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	}

	@ResponseBody
	@GetMapping("/login")
	String login() {
		return "login";
	}
}
