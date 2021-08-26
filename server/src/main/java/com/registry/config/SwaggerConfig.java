package com.registry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lee on 2017. 3. 19..
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${security.oauth2.auth-server-uri}")
    private String authServer;
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/api/**"))
                // 그중 /api/** 인 URL들만 필터링
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()));
        // 같은 url을 쓰지만 params로 분기된 메소드를 처리시켜준다.
        // .enableUrlTemplating(true);
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(",")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.none())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {
                new AuthorizationScope("read", "read all"),
                new AuthorizationScope("write", "write all")
        };

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    private OAuth securitySchema() {
        final List<AuthorizationScope> authorizationScopeList = new ArrayList<>(2);

        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
        authorizationScopeList.add(new AuthorizationScope("write", "access all"));

        final List<GrantType> grantTypes = new ArrayList<>(1);
        // 토큰 end point (http://localhost:3000/oauth/token)
        grantTypes.add(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));

        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }
}
