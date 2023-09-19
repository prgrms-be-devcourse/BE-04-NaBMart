package com.prgrms.nabmart.domain.user.service;

import static java.util.stream.Collectors.groupingBy;

import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.repository.response.UserOrderCount;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService {

    private static final int ONE = 1;

    private final UserRepository userRepository;

    @Transactional
    public void updateUserGrade() {
        LocalDateTime startTimeOfPreviousMonth = getStartTimeOfPreviousMonth();
        LocalDateTime lastTimeOfPreviousMonth = getEndTimeOfPreviousMonth();

        List<UserOrderCount> userOrderCounts = userRepository.getUserOrderCount(
            startTimeOfPreviousMonth,
            lastTimeOfPreviousMonth);
        Map<UserGrade, List<UserOrderCount>> userGradeGroup = groupByUserGrade(userOrderCounts);
        userGradeGroup.forEach(((userGrade, userOrderCountGroup) ->
            userRepository.updateUserGrade(userGrade, extractUserIds(userOrderCountGroup))));
    }

    private LocalDateTime getStartTimeOfPreviousMonth() {
        return YearMonth.now()
            .minusMonths(ONE)
            .atDay(ONE)
            .atStartOfDay();
    }

    private LocalDateTime getEndTimeOfPreviousMonth() {
        return YearMonth.now()
            .atDay(ONE)
            .atStartOfDay()
            .minusNanos(ONE);
    }

    private Map<UserGrade, List<UserOrderCount>> groupByUserGrade(
        List<UserOrderCount> userOrderCounts) {
        return userOrderCounts.stream()
            .collect(groupingBy(userOrderCount ->
                UserGrade.calculateUserGrade(userOrderCount.getOrderCount())));
    }

    private List<Long> extractUserIds(List<UserOrderCount> userOrderCountGroup) {
        return userOrderCountGroup.stream()
            .map(UserOrderCount::getUserId)
            .toList();
    }
}
