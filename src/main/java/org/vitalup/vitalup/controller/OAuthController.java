package org.vitalup.vitalup.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class OAuthController {

  @GetMapping("/api/user/info")
  public Map<String, Object> userInfo(OAuth2AuthenticationToken authentication) {
    assert authentication.getPrincipal() != null;
    return authentication.getPrincipal().getAttributes();
  }
}
