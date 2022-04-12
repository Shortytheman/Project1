package com.example.project1.controller;


import com.example.project1.model.Item;
import com.example.project1.repository.WebshopRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    model.addAttribute("items", webshopRepository.showItems());
    model.addAttribute("total",webshopRepository.total());
    return "basket";
  }

  //@PostMapping("/basket")
  //public String basket1(@PathVariable("name") String name, Model model){
  //Metoder som skal gøre noget skal ind her
  // return "redirect:/";
  //}

  @GetMapping("/addAnother/{name}")
  public String addAnother(@PathVariable("name") String name){
    webshopRepository.addAnother(name);
    return "redirect:/basket";
  }

  @GetMapping("/delete/{name}")
  public String delete(@PathVariable("name") String name){
    webshopRepository.deleteItem(name);
    return "redirect:/basket";
  }

}
