package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("intro")
    public String hello(Model model){
        // add attribute to the model and send it to view
        model.addAttribute("data", "hello!!!");

        // name of the view - hello.html
        return "hello";
    }
}
