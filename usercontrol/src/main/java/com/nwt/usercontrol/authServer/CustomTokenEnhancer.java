package com.nwt.usercontrol.authServer;

import java.util.HashMap;
import java.util.Map;

import com.nwt.usercontrol.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import org.springframework.security.core.userdetails.User;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
        com.nwt.usercontrol.model.User u = userRepository.findByMail(user.getUsername());
        u.setPassword("[REDACTED]");
        additionalInfo.put("client", u);

        // additionalInfo.put("client", userRepository.findByMail(user.getUsername()));

        ((DefaultOAuth2AccessToken) accessToken)
                .setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}