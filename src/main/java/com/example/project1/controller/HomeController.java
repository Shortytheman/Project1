package com.example.project1.controller;


import com.example.project1.repository.WebshopRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  WebshopRepository webshopRepository;

  public HomeController(WebshopRepository webshopRepository){
    this.webshopRepository = webshopRepository;
  }

  @GetMapping("/")
  public String index(){
    return "index";
  }

  @GetMapping("/basket")
  public String basket(Model model){
    model.addAttribute("items", webshopRepository.showAll());
    return "basket";
  }
}
