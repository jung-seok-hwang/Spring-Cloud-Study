package cloud.springcloudstudyuserservice.user.controller;


import cloud.springcloudstudyuserservice.user.dto.UserDto;
import cloud.springcloudstudyuserservice.user.vo.request.UserRequest;
import cloud.springcloudstudyuserservice.user.vo.response.UserResponse;
import cloud.springcloudstudyuserservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final UserService userService;

    @GetMapping("/hello")
    public String test() {

        return String.format("It's Working in User Service on Port %s", env.getProperty("local.server.port"));

    }

    @GetMapping("/")
    public String init(@RequestParam(required = false) String continueUrl) {
        // 인증 정보를 로깅하고 continueUrl 파라미터에 따라 다른 처리를 할 수 있음
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info("Authentication = {}", authentication);
        if (continueUrl != null) {
            log.info("Continue to: {}", continueUrl);
            // 필요하다면 여기서 리디렉션 로직을 추가할 수 있습니다.
        }
        return "index";
    }

    @GetMapping("/check")
    public String status() {

        return String.format(
                "port(local.sever.port) = ", env.getProperty("local.server.port")
        );

    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest users) {

        log.info("Model Mapper User Information = {}" , users.toString());
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto map = mapper.map(users, UserDto.class);
        log.info("Model Mapper User Information = {}" , map);

        UserResponse user = userService.createUser(map);

        return ResponseEntity.ok().body(user);

    }
}
