package com.ssafy.shalendar.springboot.config;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 로그인, 로그아웃 세션 관려된 설정(Config, SpringSecurity)
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests() //페이지 권한 설정
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    // user 만 접근 가능
                    // .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // 모든 페이지를 접근 가능자만 접근 가능하게
                    // .anyRequest().authenticated()
                 .and()
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()  // 로그 아웃 설정
                    .logout()
                    .logoutSuccessUrl("/");
    }
}