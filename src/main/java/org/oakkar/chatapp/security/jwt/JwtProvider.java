package org.oakkar.chatapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.oakkar.chatapp.security.principal.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {
    @Value("${app.jwt.secret}")
    private String SECRET;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final String authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder().setId(Long.toString(userPrincipal.getId())).claim("roles", authorities)
                .setIssuedAt(new Date(new Date().getTime()))
                .setIssuer("HtetOakkar")
                .setSubject(userPrincipal.getEmail())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(String token) {
        return buildJwt(token).getBody();
    }

    private Jws<Claims> buildJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public boolean validateJwtToken(String token) {
        try {
            buildJwt(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {}", e);
        }
        return false;
    }
}
