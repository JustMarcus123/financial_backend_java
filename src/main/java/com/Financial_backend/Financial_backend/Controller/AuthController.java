package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.UserLoginRequestDto;
import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserLoginResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import com.Financial_backend.Financial_backend.Service.Auth.AuthServiceImplementation;
import com.Financial_backend.Financial_backend.Service.Users.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImplementation authServiceImplementation;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    public AuthController (AuthServiceImplementation authServiceImplementation , JwtService jwtService, UsersRepository usersRepository){
        this.authServiceImplementation =authServiceImplementation;
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto,
            HttpServletRequest httpRequest,
            HttpServletResponse httpServletResponse
    ) {
        UserLoginResponseDto response = authServiceImplementation.login(userLoginRequestDto, httpRequest);

        // ✅ use ResponseCookie instead of Cookie — supports SameSite
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                .httpOnly(true)
                .secure(false)          // true in production
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Lax")        // ✅ required for cross-origin cookie sending
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")        // ✅ required
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of(
                "email", response.getEmail(),
                "role", response.getRole(),
                "firstName", response.getFirstName(),
                "message", response.getMessage()
        ));
    }
    //POST /api/auth/refresh
    //Frontend calls this silently when the access token is expired
    //Body ---- refreshToken: uuid-string
    //Returns:{accessToken,refreshToken,email,message}


    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest httpServletRequest) {
        // ✅ null check first
        if (httpServletRequest.getCookies() == null) {
            return ResponseEntity.status(401).body("no valid session");
        }

        String accessToken = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("accessToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (accessToken == null || !jwtService.isTokenValid(accessToken)) {
            return ResponseEntity.status(401).body("no valid session");
        }

        String email = jwtService.extractEmail(accessToken);
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        if (httpServletRequest.getCookies() != null) {
            Arrays.stream(httpServletRequest.getCookies())
                    .filter(c -> c.getName().equals("refreshToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresent(authServiceImplementation::logout);
        }

        // ✅ maxAge(0) actually deletes the cookie
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)  // ✅ 0 = delete
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)  // ✅ 0 = delete
                .sameSite("Lax")
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletRequest request,
            HttpServletResponse httpServletResponse
    ) {
        if (request.getCookies() == null) {
            return ResponseEntity.status(401).body("No cookies found");
        }

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("No refresh token found");
        }

        UserLoginResponseDto response = authServiceImplementation.refresh(refreshToken);

        // ✅ use ResponseCookie for consistency
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Lax")
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok(Map.of(
                "email", response.getEmail(),
                "message", response.getMessage()
        ));
    }


}

