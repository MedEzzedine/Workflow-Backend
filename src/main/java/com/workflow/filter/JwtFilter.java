package com.workflow.filter;


import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock.Catch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.workflow.entity.User;
import com.workflow.service.CustomUserDetailsService;
import com.workflow.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.Console;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {


	@Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService service;

    

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    	 
    	
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;
        try {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(userName);
        

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        
     }catch(ExpiredJwtException eje) {
    	 //log.info("Security exception for user {} - {}",
    	 //eje.getClaims().getSubject(), eje.getMessage());
    	 //log.trace("Security exception trace: {}", eje);
    	 ((HttpServletResponse) httpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
     }
    	
    }
   
}