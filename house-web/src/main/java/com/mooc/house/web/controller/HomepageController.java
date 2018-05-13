package com.mooc.house.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {
  
  /*@Autowired
  private RecommendService recommendService;
  
  @RequestMapping("index")
  public String accountsRegister(ModelMap modelMap){
    List<House> houses =  recommendService.getLastest();
    modelMap.put("recomHouses", houses);
    return "/homepage/index";
  }*/
  

  @RequestMapping("")
  public String index(ModelMap modelMap){
    return "redirect:/index";
  }
}
