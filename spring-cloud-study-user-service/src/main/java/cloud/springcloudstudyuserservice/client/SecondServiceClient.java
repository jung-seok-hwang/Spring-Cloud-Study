package cloud.springcloudstudyuserservice.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "second-service")
public interface SecondServiceClient {

    @GetMapping("/second-service/welcome")
    ResponseEntity<String> bySeverB();
}
