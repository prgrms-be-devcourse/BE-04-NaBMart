package com.prgrms.nabmart.domain.delivery.service;

import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.exception.DuplicateRiderUsernameException;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.request.RiderSignupCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final PasswordEncoder passwordEncoder;
    private final RiderRepository riderRepository;

    @Transactional
    public Long riderSignup(RiderSignupCommand riderSignupCommand) {
        checkUsernameDuplication(riderSignupCommand.username());
        Rider rider = createRider(riderSignupCommand);
        riderRepository.save(rider);
        return rider.getRiderId();
    }

    private void checkUsernameDuplication(final String username) {
        if (riderRepository.existsByUsername(username)) {
            throw new DuplicateRiderUsernameException("사용할 수 없는 아이디입니다.");
        }
    }

    private Rider createRider(RiderSignupCommand riderSignupCommand) {
        String encodedPassword = passwordEncoder.encode(riderSignupCommand.password());
        return Rider.builder()
            .username(riderSignupCommand.username())
            .password(encodedPassword)
            .address(riderSignupCommand.address())
            .build();
    }
}
