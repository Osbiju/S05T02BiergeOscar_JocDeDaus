package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.AuthenticationResponse;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignInDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignUpDTO;

public interface AuthenticationService {
    AuthenticationResponse signUp (SignUpDTO request);

    AuthenticationResponse signIn (SignInDTO request);
}
