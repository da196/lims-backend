package tz.go.tcra.lims.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.utils.exception.AuthException;

@Service
@Slf4j
public class JwtUtility {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.milliseconds}")
    private int jwtExpiration;
    
    public String extractUsername(String token) {
        try{
        
            return extractClaim(token, Claims::getSubject);
            
        }catch(MalformedJwtException e){
        
            throw new AuthException("MALFORMED jwt token");
        }catch(ExpiredJwtException e){
        
            throw new AuthException("EXPIRED JWT TOKEN");
        }catch(SignatureException e){
        
            throw new AuthException("JWT SIGNATURE ERROR");
        }catch(IllegalArgumentException e){
        
            throw new AuthException("UNAUTHORIZED");
        }catch(UnsupportedJwtException e){
        
            throw new AuthException("UNAUTHORIZED");
        }
        
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, Map<String, Object> claims) {

        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

