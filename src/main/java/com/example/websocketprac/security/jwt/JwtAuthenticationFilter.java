package com.example.websocketprac.security.jwt;

import com.example.websocketprac.dto.LoginInfo;
import com.example.websocketprac.security.user.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // jwt 생성하기 위해
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/api/user/login"); // 로그인 url 설정
    }

    // 로그인 시도
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // json 형태의 string 데이터를 객체로 변환 (request의 body 속 username, password -> dto)
            LoginInfo requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginInfo.class);

            // 인증 처리
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getName(),
                            null,
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공
    // Authentication 인증 객체 받아옴 -> 그 속의 UserDetailsImpl username 가져오기
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String name = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        // token 생성
        String token = jwtTokenProvider.generateToken(name);
        // header 에 바로 넣어줌
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
    }

    // 로그인 실패
    // 반환 메세지 작성 시 여기서
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); // unauthorized
    }
}
