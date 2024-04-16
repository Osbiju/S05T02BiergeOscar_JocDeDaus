package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.implementations;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.UserRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findById(username).orElseThrow(()-> new EntityNotFoundException("User not found."));
    }
}
