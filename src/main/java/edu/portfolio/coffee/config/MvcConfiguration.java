package edu.portfolio.coffee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/&&").allowedOrigins("*");
    }// addMapping("/api/&&"): CORS 설정을 /api/&& 경로에 적용
  //  allowedOrigins("*"): 이 경로에 대해 모든 도메인에서의 요청을 허용

}
// MVC 설정을 커스터마이징
// CORS(Cross-Origin Resource Sharing) 설정을 커스터마이징할 수 있는 메서드
// CorsRegistry 객체를 사용하여 특정 경로에 대해 CORS 정책을 설정