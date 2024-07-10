package cloud.springcloudstudyuserservice.user.service;

import cloud.springcloudstudyuserservice.user.dto.UserDto;
import cloud.springcloudstudyuserservice.user.vo.response.UserResponse;
import cloud.springcloudstudyuserservice.user.entity.User;
import cloud.springcloudstudyuserservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
}
