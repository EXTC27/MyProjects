package com.ssafy.shalendar.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println(request.getMethod() + " : " + request.getServletPath());
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        } else {
            String token = request.getHeader("jwt");
            if (token != null && token.length() > 0) {
                jwtService.checkValid(token);
                log.trace("토큰 사용 가능 : {}" ,token);
                return true;
            } else {
                throw new RuntimeException("인증 토큰이 없습니다.");
            }
        }
    }
}
