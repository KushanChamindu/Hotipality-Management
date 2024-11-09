package com.example.megablissapigatewayservice.Filter;

import com.example.megablissapigatewayservice.Util.JwtService;
import com.example.megablissapigatewayservice.dao.mapper.ErrorMapper;
import com.example.megablissapigatewayservice.exception.CustomError;
import com.example.megablissapigatewayservice.exception.UnauthorisedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AutenticationFilter extends AbstractGatewayFilterFactory<AutenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtService jwt;

    @Autowired
    private ErrorMapper errorMapper;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public AutenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // headers contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    // Handle invalid token exception
                    return setErrorException(exchange.getResponse(),
                            new UnauthorisedException("Not Authorised from Gateway",
                                    "AUTHORIZATION_TOKEN_NOT_FOUND", HttpStatus.FORBIDDEN));
                }
                String role = null;
                String authheaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authheaders != null && authheaders.startsWith("Bearer ")) {
                    authheaders = authheaders.substring(7);
                    // role=
                    // restTemplate.getForObject("http://localhost:9090/auth/getSessionByToken?token="+authheaders,
                    // String.class);
                }
                try {
                    jwt.validateToken(authheaders);
                } catch (IllegalArgumentException e) {
                    log.error("An error occurred while fetching Username from Token");
                    return setErrorException(exchange.getResponse(),
                            new UnauthorisedException("Invalide access token",
                                    "NOT_AUTHORISED", HttpStatus.UNAUTHORIZED));
                } catch (ExpiredJwtException e) {
                    log.warn("The token has expired");
                    return setErrorException(exchange.getResponse(),
                            new UnauthorisedException("Access token has expired !",
                                    "NOT_AUTHORISED", HttpStatus.FORBIDDEN));
                } catch (Exception e) {
                    log.warn("Something wrong in the access token");
                    // Handle invalid token exception
                    return setErrorException(exchange.getResponse(),
                            new UnauthorisedException("Something wrong in the access token !",
                                    "NOT_AUTHORISED", HttpStatus.UNAUTHORIZED));

                }

            }

            return chain.filter(exchange);
        });
    }
    public static class Config {

    }

    private Mono<Void> setErrorException(ServerHttpResponse response, UnauthorisedException error) {

        try {
            CustomError customError = errorMapper.mapToCustomError(error, error.getHttpStatus());
            customError.setError(error.getErrorCode());
            byte[] bits = objectWriter.writeValueAsBytes(customError);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(error.getHttpStatus());
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.debug("failed to process json", e);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }

    }
}