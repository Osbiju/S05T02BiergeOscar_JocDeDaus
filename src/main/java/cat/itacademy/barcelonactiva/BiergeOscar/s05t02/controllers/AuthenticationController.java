package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.AuthenticationResponse;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignInDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignUpDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Inici programa Joc de Daus", description = "Operacions pertinents al inici de sessió")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    @Operation(summary = "Crea un compte per accedir al joc", description = "Aquesta API permet crear un nou Usuari")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpDTO signUp){
        return ResponseEntity.ok(authenticationService.signUp(signUp));
    }

    @PostMapping("/signin")
    @Operation(summary = "Accedir amb email i pw", description = "Aquesta API permet accedir al joc menres tinguis email y pw creats")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInDTO signIn) {
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }
}
