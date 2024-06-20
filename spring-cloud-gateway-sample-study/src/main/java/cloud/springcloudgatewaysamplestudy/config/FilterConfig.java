package cloud.springcloudgatewaysamplestudy.config;



import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;


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
                .route("rewrite_request_upper", r -> r
                        .host("*.rewriterequestupper.org")
                        .and()
                        .path("/first-service/**")
                        .filters(f -> f.prefixPath("/message")
                                .addResponseHeader("X-TestHeader", "rewrite_request_upper")
                                .modifyRequestBody(String.class, String.class, (exchange, s) -> Mono.just(s.toUpperCase() + s.toUpperCase()))
                        ).uri(URL))


                .build();
    }

}
