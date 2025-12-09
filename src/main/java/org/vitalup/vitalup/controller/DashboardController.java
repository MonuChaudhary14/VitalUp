package org.vitalup.vitalup.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

  @GetMapping("/dashboard")
  public String dashboard(@AuthenticationPrincipal OAuth2User principal, Model model) {
    model.addAttribute("username", principal.getAttribute("name"));
    model.addAttribute("email", principal.getAttribute("email"));
    return "dashboard";
  }
}
