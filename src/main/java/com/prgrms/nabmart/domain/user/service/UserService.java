package com.prgrms.nabmart.domain.user.service;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.DoesNotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.request.RegisterUserCommand;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public RegisterUserResponse getOrRegisterUser(final RegisterUserCommand registerUserCommand) {
        User findUser = userRepository.findByProviderAndProviderId(
                registerUserCommand.provider(),
                registerUserCommand.providerId())
            .orElseGet(() -> {
                User user = User.builder()
                    .nickname(registerUserCommand.nickname())
                    .email(registerUserCommand.email())
                    .provider(registerUserCommand.provider())
                    .providerId(registerUserCommand.providerId())
                    .userRole(registerUserCommand.userRole())
                    .build();
                userRepository.save(user);
                return user;
            });
        return RegisterUserResponse.from(findUser);
    }

    @Transactional(readOnly = true)
    public FindUserDetailResponse findUser(FindUserCommand findUserCommand) {
        User findUser = findUserByUserId(findUserCommand.userId());
        return FindUserDetailResponse.from(findUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new DoesNotFoundUserException("존재하지 않는 유저입니다."));
    }
}
