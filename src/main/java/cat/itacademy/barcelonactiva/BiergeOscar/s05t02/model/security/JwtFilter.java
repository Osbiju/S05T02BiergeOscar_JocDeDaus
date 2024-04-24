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
@RequiredArgsConstructor //it will create a constr using a final field as atribut
public class JwtFilter extends OncePerRequestFilter {
    //we can use UserDetailsService from pringsecurity to not create UserService Class
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;//jwtToken
        final String email;

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        //extract token from header
        jwt = authorizationHeader.substring(7);//7 for the "Bearer "
        email = jwtService.getUserName(jwt);//TO extract email from JWT token, we need jwtservice
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){//if getAuthentication is null it means that is not yet authenticated
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
