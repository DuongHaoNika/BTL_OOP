package com.project.web.filters;

import com.project.web.components.JwtTokenUtil;
import com.project.web.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
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
        try {
            if(isBypassToken(request)) {
                if(getCookie(request, "token") == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            final String token = getCookie(request, "token");
            final String username = jwtTokenUtil.extractUsername(token);
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(username);
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            request.setAttribute("username", username);
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Hello");
        }
    }

    private boolean isBypassToken(HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens = Arrays.asList (
                Pair.of("/auth/register", "GET"),
                Pair.of("/auth/login", "GET"),
                Pair.of("/auth/login", "POST"),
                Pair.of("/auth/register", "POST"),
                Pair.of("/css/style.css", "GET"),
                Pair.of("/about", "GET"),
                Pair.of("/", "GET"),
                Pair.of("/img", "GET"),
                Pair.of("/contact", "GET"),
                Pair.of("/auth/logout", "GET"),
                Pair.of("/post", "GET"),
                Pair.of("/uploads", "GET")
        );
        for(Pair<String, String> token : bypassTokens){
            if(request.getServletPath().equals("/")) {
                return true;
            }
            if(request.getServletPath().contains(token.getFirst()) &&
                    request.getMethod().equals(token.getSecond())){
                System.out.println(request.getServletPath());
                return true;
            }
        }
        return false;
    }

    private String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
