package cloud.springcloudgatewaysamplestudy.config;



import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import reactor.core.publisher.Mono;

import java.util.Map;


@Configuration
public class FilterConfig {

    private static final String URL = "http://localhost:8081";

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/first-service/**")
                        .filters(f -> f.addRequestHeader("first-request","first-request-header")
                                        .addResponseHeader("first-response","first-response-header")
                        )
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request","second-request-header")
                                .addResponseHeader("second-response","second-response-header"))
                        .uri("http://localhost:8082"))
                .route("rewrite_request_obj", r -> r.host("*.rewriterequestobj.org")
                        .filters(f -> f.prefixPath("/first-service/message")
                                .addResponseHeader("X-TestHeader", "read_body_pred")
                        ).uri(URL))
                .route("rewrite_request_upper", r -> r.host("*.rewriterequestupper.org")
                        .filters(f -> f.prefixPath("/httpbin")
                                .addResponseHeader("X-TestHeader", "rewrite_request_upper")
                                .modifyResponseBody(Map.class, String.class, (exchange, s) -> {
                                    if (s == null) {
                                        return Mono.error(new IllegalArgumentException("this should not happen"));
                                    }
                                    Object message = s.get("message");
                                    Object name = s.get("name");
                                    return Mono.just(message.toString().toUpperCase() + message.toString().toUpperCase());
                                })
                                .setResponseHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                        ).uri(URL))
                .build();
    }

}
