package cloud.springcloudgatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("요청 정보 = {}", request);
            log.info("응답 정보 = {}", response);
            log.info("요청 URL = {}", request.getURI());
            log.info("exchange URL 정보 = {}", exchange.getRequest().getURI());
            log.info("exchange 정보 = {}", exchange);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        log.info("Custom Post filter : response code = {}", response.getStatusCode());
                    }
            ));
        });

    }


    public CustomFilter() {
        super(Config.class);
    }

    public static class Config {

    }
}
