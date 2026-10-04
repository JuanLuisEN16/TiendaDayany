package grace_by_dayany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller 
public class HomeController {
    
    @GetMapping("/")
    public String iniciado(){
        return "index";
    }


}
