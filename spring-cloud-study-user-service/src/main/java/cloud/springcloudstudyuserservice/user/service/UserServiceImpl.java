package cloud.springcloudstudyuserservice.user.service;

import cloud.springcloudstudyuserservice.client.SecondServiceClient;
import cloud.springcloudstudyuserservice.user.dto.UserDto;
import cloud.springcloudstudyuserservice.user.vo.response.UserResponse;
import cloud.springcloudstudyuserservice.user.entity.User;
import cloud.springcloudstudyuserservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecondServiceClient secondServiceClient;
    private final PlatformTransactionManager transactionManager;


    @Override
    public UserResponse createUser(UserDto userDto) {
        userDto.userSecretId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(AccessLevel.PRIVATE)  // 필드 직접 접근 활성화
                .setFieldMatchingEnabled(true);

        userDto.secretEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = mapper.map(userDto, User.class);

        log.info("사용자 정보 = {}" , user.toString());

        userRepository.save(user);

        return UserResponse.of(user);
    }


    public String doSpringCloudUserService() {

        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionAttribute());

        try {
            String status = transactionStatus.getTransactionName();
            log.info("트랜잭션 정보 = {}", status);

            ResponseEntity<String> stringResponseEntity = secondServiceClient.bySeverB();

            // 비즈니스 로직 수행 후 커밋
//            transactionManager.commit(transactionStatus);
            transactionManager.rollback(transactionStatus);

            return stringResponseEntity.getBody();
        } catch (Exception e) {
            throw e;  // 예외를 다시 던져서 호출자에게 알림
        }
    }
}
