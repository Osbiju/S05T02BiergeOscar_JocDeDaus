package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.AuthenticationResponse;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignInDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignUpDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpDTO signUp){
        return ResponseEntity.ok(authenticationService.signUp(signUp));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInDTO signIn) {
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }
}
