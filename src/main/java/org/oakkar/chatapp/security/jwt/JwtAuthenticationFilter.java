package org.oakkar.chatapp.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oakkar.chatapp.security.principal.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private String extractAuthToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer")? header.replace("Bearer ", "") : null;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractAuthToken(request);
            if (token != null && jwtProvider.validateJwtToken(token)) {
                Claims claims = jwtProvider.getClaims(token);
                String email = claims.getSubject();
                long userId = Long.parseLong(claims.getId());
                String roles = claims.get("roles", String.class);
                List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                UserPrincipal userPrincipal = new UserPrincipal(userId, null, null, email, authorities);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities));

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }
}
