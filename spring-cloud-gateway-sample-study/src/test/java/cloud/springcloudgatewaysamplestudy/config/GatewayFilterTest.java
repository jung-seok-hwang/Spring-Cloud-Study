package cloud.springcloudgatewaysamplestudy.config;


import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.client.DefaultServiceInstance;

import org.springframework.cloud.gateway.config.GatewayMetricsProperties;
import org.springframework.cloud.gateway.test.HttpBinCompatibleController;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.ServiceInstanceListSuppliers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(classes = {GatewayFilterTest.TestConfig.class}, webEnvironment = DEFINED_PORT,
        properties = {"management.endpoint.gateway.enabled=true", "management.server.port=${test.port}", "server.port=8000"})
public class GatewayFilterTest {

    protected static int managementPort;//관리 port 번호

    private static final String URI = "/first-service";

    @Autowired
    GatewayMetricsProperties metricsProperties;//재산 관리

    @LocalServerPort
    private int port = 8000;

    protected WebTestClient client;

    protected String baseUri;

    @BeforeAll
    public static void beforeClass() {
        managementPort = TestSocketUtils.findAvailableTcpPort();

        System.setProperty("test.port", String.valueOf(managementPort));
    }

    @BeforeEach
    public void before() {
        baseUri = "http://localhost:" + port;
        this.client = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10))
                .baseUrl(baseUri)
                .build();
    }

    @Test
    public void test() {
        client.get().uri(URI + "/welcome").exchange().expectStatus().isOk();
    }

    @Test
    public void rewriterequestupperHostRequest() {

        MessageRequest message = MessageRequest.builder().message("Test Message").name("John Doe").build();

        client.post().uri(URI + "/message")
                .header("Host", "ww.rewriterequestupper.org")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchange()
                .expectStatus().isOk().expectHeader()
                .valueEquals("X-TestHeader", "rewrite_request_upper")
                .expectBody(String.class)
                .consumeWith(request -> assertThat(request.getResponseBody()).isEqualTo("수신 완료"));
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MessageRequest {
        private String message;
        private String name;

    }

    //"I learned how to use this code from a Spring Boot documentation example."
    //Git address : https://github.com/spring-cloud/spring-cloud-gateway/blob/main/spring-cloud-gateway-sample/src/test/java/org/springframework/cloud/gateway/sample/GatewaySampleApplicationTests.java
    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @LoadBalancerClient(name = "httpbin", configuration = LoadBalancerConfig.class)
    @Import(FilterConfig.class)
    protected static class TestConfig {

        @Bean
        public HttpBinCompatibleController httpBinCompatibleController() {
            return new HttpBinCompatibleController();
        }

    }
    protected static class LoadBalancerConfig {

        @LocalServerPort
        int port;

        @Bean
        public ServiceInstanceListSupplier fixedServiceInstanceListSupplier(Environment env) {
            return ServiceInstanceListSuppliers.from("httpbin",
                    new DefaultServiceInstance("httpbin-1", "httpbin", "localhost", port, false));
        }

    }


}
