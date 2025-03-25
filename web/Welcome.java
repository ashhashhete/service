package com.tus.uploadservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class Welcome {

	
	@GetMapping("/get")
    public String welcome() {
        return "tus upload service";
    
}
}