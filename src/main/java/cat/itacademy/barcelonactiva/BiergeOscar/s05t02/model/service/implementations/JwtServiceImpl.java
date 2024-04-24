package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.implementations;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    //https://asecuritysite.com/encryption/plain
    //it can be placed in properties too and here is needed:
    //@Value("${token.signin.key}
    @Value("${token.signing.key}")
    private String SECRET_KEY;

    @Override
    public String getUserName(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public String tokenGenerator(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean validToken(String token, UserDetails userDetails) {
        final String username = getUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = getAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().claims().add(extraClaims).subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .and()
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();//compact generate and return the token
    }

    private boolean isTokenExpired(String token){ //if the token validates the userDetails
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey()) //signInKey is a secret key that is used to digitally sign the Jwt, is used to create the signature part of the jwt which is use to verify that the sender of jwt is who is claim to be and ensure that the message is not change along the way
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
