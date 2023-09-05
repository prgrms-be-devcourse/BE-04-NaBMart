package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.response.FindItemResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private static final double REVIEW_WEIGHT = 0.4;
    private static final double RATING_WEIGHT = 0.3;
    private static final double LIKE_WEIGHT = 0.3;


    private List<FindItemResponse> orderByPopularity(List<FindItemResponse> items) {
        Comparator<FindItemResponse> popularityComparator = new Comparator<FindItemResponse>() {
            @Override
            public int compare(FindItemResponse item1, FindItemResponse item2) {
                double popularity1 = calculatePopularity(item1);
                double popularity2 = calculatePopularity(item2);

                if (popularity1 < popularity2) {
                    return 1;
                } else if (popularity1 > popularity2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(items, popularityComparator);
        return items;
    }

    private double calculatePopularity(FindItemResponse item) {
        double reviewScore = item.reviewCount() * REVIEW_WEIGHT;
        double ratingScore = item.rate() * RATING_WEIGHT;
        double likeScore = item.like() * LIKE_WEIGHT;

        return reviewScore + ratingScore + likeScore;
    }
}
