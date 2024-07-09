package cloud.springcloudstudyuserservice.user.service;

import cloud.springcloudstudyuserservice.user.controller.request.UserDto;
import cloud.springcloudstudyuserservice.user.controller.request.UserRequest;
import cloud.springcloudstudyuserservice.user.controller.response.UserResponse;
import cloud.springcloudstudyuserservice.user.entity.User;
import cloud.springcloudstudyuserservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User user = mapper.map(userDto, User.class);

        log.info("사용자 정보 = {}" , user.toString());

        user.updatePassword(passwordEncoder.encode(userDto.getPassword()) , UUID.randomUUID().toString());

        userRepository.save(user);

        return UserResponse.of(user);
    }
}
