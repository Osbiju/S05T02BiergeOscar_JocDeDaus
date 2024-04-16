package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String getUserName(String token);
    String tokenGenerator(UserDetails userDetails);
    boolean validToken(String token, UserDetails userDetails);
}
