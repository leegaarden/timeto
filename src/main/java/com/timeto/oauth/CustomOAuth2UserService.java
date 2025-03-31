package com.timeto.oauth;

import com.timeto.domain.User;
import com.timeto.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
            // 필요하다면 기존 사용자 정보 업데이트
            user.setName((String) attributes.get("name"));
            user = userRepository.save(user);
        } else {
            // 신규 사용자 생성
            user = User.builder()
                    .name((String) attributes.get("name"))
                    .email(email)
                    .build();
            user = userRepository.save(user);
        }

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
