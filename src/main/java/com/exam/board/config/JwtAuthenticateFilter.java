package com.exam.board.config;

import com.exam.board.exception.jwt.JwtTokenNotFoundException;
import com.exam.board.service.JwtService;
import com.exam.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class JwtAuthenticateFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //시큐리티는 수많은 필터 체인으로 구성,
        // 이 필터가 여러번 수행되지 않게 설정

        //TODO: JWT 검증
        // 그럼 jwt 토큰을 어떻게 받아오냐?
        // http 헤더에서 가져오는 로직
        String BEARER_PREFIX="Bearer ";
        var authorization= request.getHeader(HttpHeaders.AUTHORIZATION);

        //인증이 완료되면 이 컨텍스트에 저장하는데
        //이미 여기에 인증정보가 있다면 증명할 필요없음
        var securityContext= SecurityContextHolder.getContext();


        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith(BEARER_PREFIX)
        && securityContext.getAuthentication()==null){

            //흐름을 기억해
            var accessToken= authorization.substring(BEARER_PREFIX.length());
            var username= jwtService.getUsername(accessToken);
            var userDetails= userService.loadUserByUsername(username);


            //인증이 끝났으니 사용자 인증 정보를 context에 저장
            var authenticationToken= new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }


        //검증이 끝난 후엔 이후 필터 적용
        filterChain.doFilter(request,response);
    }
}
