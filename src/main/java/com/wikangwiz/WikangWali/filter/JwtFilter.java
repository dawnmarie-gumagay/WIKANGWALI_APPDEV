package com.wikangwiz.WikangWali.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wikangwiz.WikangWali.Repository.UserRepository;
import com.wikangwiz.WikangWali.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ((!StringUtils.hasText(header)) || (StringUtils.hasText(header) && !header.startsWith("Bearer "))) {
            chain.doFilter(request, response);
            return;
        }
        
        // Authorization -> ([Bearer], [fsdfwryhgriesujhfbyniwsehnfgisk])
        final String token = header.split(" ")[1].trim();

        // Get user identity and set it on the spring security context
        UserDetails userDetails = urepo
            .findByUsername(jwtUtil.getUsernameFromToken(token))
            .orElse(null);
        
        // Get jwt token and validate
        if (!jwtUtil.validateToken(token, userDetails)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                    List.of() : userDetails.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        //Authentication happens, and the user is now valid
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        chain.doFilter(request, response);
    }

}
