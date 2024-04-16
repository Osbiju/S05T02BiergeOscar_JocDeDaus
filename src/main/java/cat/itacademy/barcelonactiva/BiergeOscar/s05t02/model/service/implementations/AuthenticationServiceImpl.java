package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.implementations;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.AuthenticationResponse;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignInDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.SignUpDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.User;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.enums.Role;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.UserRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.AuthenticationService;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signUp(SignUpDTO request) {
        if(request.getEmail().isEmpty() || request.getPassword().isEmpty()){
            throw new IllegalArgumentException("Email and password can't be null.");
        }
        userRepository.findById(request.getEmail())
                .ifPresent(user -> {throw new EntityExistsException("Email is already registered.");
                });
        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        String jwt = jwtService.tokenGenerator(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public AuthenticationResponse signIn(SignInDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findById(request.getEmail()).orElseThrow(()-> new EntityNotFoundException("Email not found."));
        String jwt = jwtService.tokenGenerator(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }
}
