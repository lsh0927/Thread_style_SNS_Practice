package com.exam.board.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger= LoggerFactory.getLogger(JwtService.class);
    private static final SecretKey key=  Jwts.SIG.HS256.key().build();

    private String generateToken(String subject){

        var now= new Date();
        var exp= new Date(now.getTime()+(1000*60*60*3));
        return  Jwts.builder().subject(subject).signWith(key).issuedAt(now).expiration(exp).compact();
    }
    private String getSubject(String token){
        //실패한다면 예외를 발생시켜야 함
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        }catch (JwtException e){
            //JwtAuthenticationFilter를 처리하는 과정에서 예외가 발생했을때,
            //커스텀 필터를 통하도록
            throw  e;
        }
    }

    //이제 username을 받아 토큰 발행, 토큰 검증시 username을 추출
    public String generateAccessToken(UserDetails userDetails){
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String accessToken){
        return getSubject(accessToken);
    }
}
