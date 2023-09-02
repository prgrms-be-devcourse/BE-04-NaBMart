package com.prgrms.nabmart.domain.user.service;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.DoesNotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.domain.user.service.response.AuthUserResponse;
import com.prgrms.nabmart.domain.user.service.request.RegisterOAuthUserCommnad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public AuthUserResponse getOrRegisterUser(final RegisterOAuthUserCommnad registerUserCommand) {
        User findUser = userRepository.findByProviderAndProviderId(
                registerUserCommand.provider(),
                registerUserCommand.providerId())
            .orElseGet(() -> {
                User user = User.builder()
                    .nickname(registerUserCommand.nickname())
                    .provider(registerUserCommand.provider())
                    .providerId(registerUserCommand.providerId())
                    .userRole(registerUserCommand.userRole())
                    .build();
                userRepository.save(user);
                return user;
            });
        return AuthUserResponse.from(findUser);
    }

    public FindUserDetailResponse findUser(FindUserCommand findUserCommand) {
        User findUser = userRepository.findById(findUserCommand.userId())
            .orElseThrow(() -> new DoesNotFoundUserException("존재하지 않는 유저입니다."));
        return FindUserDetailResponse.from(findUser);
    }
}
