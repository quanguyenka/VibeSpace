package com.qng.auth.oauth;

import com.qng.auth.entity.UserAccount;
import com.qng.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User user = super.loadUser(request);
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String providerId = user.getAttribute("sub");

        userRepository.findByEmail(email).orElseGet(() -> {
            return userRepository.save(UserAccount.builder()
                    .email(email)
                    .username(email)
                    .fullName(name)
                    .provider(UserAccount.AuthProvider.GOOGLE)
                    .providerId(providerId)
                    .role(UserAccount.Role.USER)
                    .build());
        });

        return user;
    }
}
