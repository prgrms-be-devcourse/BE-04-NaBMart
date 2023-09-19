package com.prgrms.nabmart.domain.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.repository.response.UserOrderCount;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @InjectMocks
    GradeService gradeService;

    @Mock
    UserRepository userRepository;

    @Mock
    EntityManager entityManager;

    private UserOrderCount createUserOrderCount(long userId, int orderCount) {
        return new UserOrderCount() {
            @Override
            public Long getUserId() {
                return userId;
            }

            @Override
            public Integer getOrderCount() {
                return orderCount;
            }
        };
    }

    private List<User> createUsers(int end) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < end; i++) {
            users.add(createUser(i));
        }
        return users;

    }

    private User createUser(long userId) {
        User user = UserFixture.user();
        ReflectionTestUtils.setField(user, "userId", userId);
        return user;
    }


    @Nested
    @DisplayName("updateUserGrade 메서드 실행 시")
    class UpdateUserGradeTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            List<User> users = createUsers(4);
            UserOrderCount normalCount = createUserOrderCount(users.get(0).getUserId(), 0);
            UserOrderCount vipCount = createUserOrderCount(users.get(1).getUserId(), 5);
            UserOrderCount vvipCount = createUserOrderCount(users.get(2).getUserId(), 10);
            UserOrderCount rvipCount = createUserOrderCount(users.get(3).getUserId(), 20);
            List<UserOrderCount> userOrderCounters = List.of(normalCount, vipCount, vvipCount,
                rvipCount);

            given(userRepository.getUserOrderCount(any(), any())).willReturn(
                userOrderCounters);

            //when
            gradeService.updateUserGrade();

            //then
            then(userRepository).should(times(4)).updateUserGrade(any(), any());
        }
    }
}
