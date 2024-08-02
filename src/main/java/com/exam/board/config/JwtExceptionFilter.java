package com.exam.board.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request,response);
        }
        catch (JwtException e){

            //TODO:jwt 관련 커스텀 에러메세지 생성해 response로 내려주기
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");

            var errorMap= new HashMap<String,Object>();
            errorMap.put("status", HttpStatus.UNAUTHORIZED);
            errorMap.put("message", e.getMessage());

            ObjectMapper objectMapper= new ObjectMapper();
            //직렬화후 응답으로 내려주기
            String responseJson= objectMapper.writeValueAsString(errorMap);
            response.getWriter().write(responseJson);
        }
    }
}
