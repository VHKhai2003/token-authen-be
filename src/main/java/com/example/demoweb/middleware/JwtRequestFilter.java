package com.example.demoweb.middleware;

import com.example.demoweb.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// this class is a middleware to validate JWT tokens
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // each request to protected routes must be process by doFilterInternal method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;


        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (ExpiredJwtException e) {
//                throw new CustomException(403, "Invalid token");
            }
            catch (SignatureException e) {
//                throw new CustomException(403, "Invalid token");
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // if user's authorities were modified, the system will update
             UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
             // in case jwt contain other information of userDetails likes authorities,...
             // can use the line below
             // UserDetails userDetails = User.builder().username(email).password("PROTECTED").build();

            // double check the email
            // tang cuong bao mat, ngan chan token bi danh cap, nhung co the bo qua
            // if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            // }
        }

        // with authentication object, it indicates that the users is authenticated
        // if don't have authentication object, the request to protected routes will be denied with status code 403 (automatically)
        filterChain.doFilter(request, response);
    }
}
