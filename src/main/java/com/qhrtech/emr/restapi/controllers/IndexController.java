/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Blake Dickie
 */
@Controller
public class IndexController {

  @RequestMapping("/")
  public String index(Model model) {
    return "index";
  }
}
