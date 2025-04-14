package com.timeto.auth.oauth;

import com.timeto.domain.User;
import com.timeto.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 공급자 (google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인시 키(google의 경우 "sub")
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2User의 속성
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 이메일 추출
        String email = (String) attributes.get("email");

        // DB에 사용자가 존재하는지 확인
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();

            // 탈퇴한 회원인 경우 - 재활성화
            if (!user.getActive()) {
                user.setActive(true);
                user.setDeactivatedAt(null);
                userRepository.save(user);
            }

        } else {
            // 신규 사용자 처리 (기존 로직)
            String name = oAuth2User.getAttribute("name");
            user = User.builder()
                    .email(email)
                    .name(name)
                    .build();
            userRepository.save(user);
        }

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    /**
     * 사용자 객체로부터 CustomOAuth2User 객체를 생성합니다.
     * JWT 인증 필터에서 사용됩니다.
     */
    public CustomOAuth2User createOAuth2User(User user) {
        // 기본 속성 생성
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.getId().toString());
        attributes.put("email", user.getEmail());
        attributes.put("name", user.getName());

        return new CustomOAuth2User(user, attributes);
    }
}