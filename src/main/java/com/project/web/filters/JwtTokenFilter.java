package com.project.web.filters;

import com.project.web.components.JwtTokenUtil;
import com.project.web.models.User;
import jakarta.servlet.FilterChain;
import org.springframework.data.util.Pair;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                final String token = authorizationHeader.substring(7);
                final String username = jwtTokenUtil.extractUsername(token);
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User userDetails = (User)userDetailsService.loadUserByUsername(username);
                    if(jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication
                                = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(request, response);
            }
            catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken(HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens = Arrays.asList (
                Pair.of("/api/v1/products", "GET"),
                Pair.of("/api/v1/categories", "GET"),
                Pair.of("/api/v1/users/login", "POST"),
                Pair.of("/api/v1/users/register", "POST")
        );
        for(Pair<String, String> token : bypassTokens){
            if(request.getServletPath().contains(token.getFirst()) &&
                    request.getMethod().equals(token.getSecond())){
                return true;
            }
        }
        return false;
    }
}
