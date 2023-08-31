package com.prgrms.nabmart.domain.user.service;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public RegisterUserResponse getOrRegisterUser(RegisterUserCommand registerUserCommand) {
        User findUser = userRepository.findByProviderAndProviderId(registerUserCommand.provider(), registerUserCommand.providerId())
                .orElseGet(() -> {
                    User user = new User(
                            registerUserCommand.nickname(),
                            registerUserCommand.provider(),
                            registerUserCommand.providerId(),
                            registerUserCommand.userRole()
                    );
                    userRepository.save(user);
                    return user;
                });
        return RegisterUserResponse.from(findUser);
    }
}
