package com.wwl.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author wwl
 * @date 2022/7/26 10:15
 * @desc 跨域配置
 */
@Configuration
public class CorsConfig {

    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (!CorsUtils.isCorsRequest(request)) {
                return chain.filter(ctx);
            }

            HttpHeaders requestHeaders = request.getHeaders();
            String origin = requestHeaders.getOrigin();
            HttpHeaders headersOrigin = ctx.getResponse().getHeaders();
            headersOrigin.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
            if (requestHeaders.getAccessControlRequestMethod() != null) {
                headersOrigin.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestHeaders.getAccessControlRequestMethod().name());
            }
            headersOrigin.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
            headersOrigin.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
            if (request.getMethod() == HttpMethod.OPTIONS) {
                headersOrigin.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headersOrigin.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                ctx.getResponse().setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(ctx).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                if (headers == null) {
                    return;
                }
                try {
                    if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)) {
                        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                    }
                    if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)) {
                        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                    }
                }catch (Exception ignore){}
            }));
        };
    }

}
