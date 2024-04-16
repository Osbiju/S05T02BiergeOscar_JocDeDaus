package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.security;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.JwtService;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authorizationHeader.substring(7);
        email = jwtService.getUserName(jwt);
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);
            if(jwtService.validToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
