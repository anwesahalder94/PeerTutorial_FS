package com.tutoring.controller;

import com.tutoring.model.User;
import com.tutoring.repository.UserRepository;
import com.tutoring.security.JwtUtil;
import com.tutoring.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OAuth2Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.oauth2.authorized-redirect-uris:http://localhost:3000/oauth2/redirect}")
    private String redirectUri;

    @GetMapping("/oauth2/authorize/{provider}")
    public RedirectView authorizeOAuth2(@PathVariable String provider) {
        return new RedirectView("/oauth2/authorization/" + provider);
    }

    @GetMapping("/oauth2/callback/{provider}")
    public void oauth2Callback(@PathVariable String provider,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();

            String email = extractEmail(oAuth2User, provider);
            String firstName = extractFirstName(oAuth2User, provider);
            String lastName = extractLastName(oAuth2User, provider);

            Optional<User> existingUser = userRepository.findByEmail(email);
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                user = new User();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(encoder.encode(UUID.randomUUID().toString()));
                user.setPhoneNumber("+1-000-000-0000");
                user.setRole(User.Role.STUDENT);
                user.setActive(true);
                user.setVerified(true);
                userRepository.save(user);
            }

            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            String jwt = jwtUtil.generateToken(authToken);

            String targetUrl = redirectUri + "?token=" + jwt +
                    "&userId=" + user.getId() +
                    "&email=" + user.getEmail() +
                    "&firstName=" + user.getFirstName() +
                    "&lastName=" + user.getLastName() +
                    "&role=" + user.getRole();

            response.sendRedirect(targetUrl);
        } else {
            response.sendRedirect(redirectUri + "?error=oauth_failed");
        }
    }

    private String extractEmail(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("github".equalsIgnoreCase(provider)) {
            String email = (String) attributes.get("email");
            if (email == null) {
                email = (String) attributes.get("login") + "@github.user";
            }
            return email;
        }

        return (String) attributes.get("email");
    }

    private String extractFirstName(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("google".equalsIgnoreCase(provider)) {
            return (String) attributes.get("given_name");
        } else if ("github".equalsIgnoreCase(provider)) {
            String name = (String) attributes.get("name");
            if (name != null && name.contains(" ")) {
                return name.substring(0, name.lastIndexOf(" "));
            }
            return (String) attributes.get("login");
        }

        String name = (String) attributes.get("name");
        return name != null ? name.split(" ")[0] : "User";
    }

    private String extractLastName(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("google".equalsIgnoreCase(provider)) {
            return (String) attributes.get("family_name");
        } else if ("github".equalsIgnoreCase(provider)) {
            String name = (String) attributes.get("name");
            if (name != null && name.contains(" ")) {
                return name.substring(name.lastIndexOf(" ") + 1);
            }
            return "GitHub";
        }

        String name = (String) attributes.get("name");
        if (name != null && name.contains(" ")) {
            return name.substring(name.lastIndexOf(" ") + 1);
        }
        return "User";
    }
}
