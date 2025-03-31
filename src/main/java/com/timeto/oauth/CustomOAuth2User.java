package com.timeto.oauth;

import com.timeto.domain.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final User user;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        super(Collections.emptyList(), attributes, "sub");
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getName() {
        return user.getName();
    }
}
