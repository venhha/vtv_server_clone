package hcmute.kltn.vtv.authentication;

import hcmute.kltn.vtv.authentication.request.LoginRequest;
import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.authentication.response.LoginResponse;
import hcmute.kltn.vtv.authentication.response.LogoutResponse;
import hcmute.kltn.vtv.authentication.response.RefreshTokenResponse;
import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;



    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerCustomer(@RequestBody RegisterRequest customerRequest) {

        return ResponseEntity.ok(authenticationService.register(customerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authentication(@RequestBody LoginRequest loginRequest,
                                                        HttpServletResponse response) {
        LoginResponse loginResponse = authenticationService.login(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            @RequestBody String fcmToken,
            HttpServletResponse response) {

        return ResponseEntity.ok(authenticationService.logout(refreshToken,  response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken, request, response));
    }







}
