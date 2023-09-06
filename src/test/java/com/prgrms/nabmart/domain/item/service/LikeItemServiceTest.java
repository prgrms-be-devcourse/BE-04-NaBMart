package com.prgrms.nabmart.domain.item.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import com.prgrms.nabmart.domain.item.exception.DuplicateLikeItemException;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.exception.NotFoundLikeItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.repository.LikeItemRepository;
import com.prgrms.nabmart.domain.item.service.request.DeleteLikeItemCommand;
import com.prgrms.nabmart.domain.item.service.request.RegisterLikeItemCommand;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import com.prgrms.nabmart.global.auth.exception.AuthorizationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LikeItemServiceTest {

    @InjectMocks
    LikeItemService likeItemService;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    LikeItemRepository likeItemRepository;

    MainCategory mainCategory = CategoryFixture.mainCategory();
    SubCategory subCategory = CategoryFixture.subCategory(mainCategory);
    User user = UserFixture.user();
    Item item = ItemFixture.item(mainCategory, subCategory);
    LikeItem likeItem = ItemFixture.likeItem(user, item);

    @Nested
    @DisplayName("registerLikeItem 메서드 실행 시")
    class RegisterLikeItemTest {

        RegisterLikeItemCommand registerLikeItemCommand
            = RegisterLikeItemCommand.of(1L, 1L);

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(item));

            //when
            likeItemService.registerLikeItem(registerLikeItemCommand);

            //then
            then(likeItemRepository).should().save(any());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 User")
        void throwExceptionWhenNotFoundUser() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> likeItemService.registerLikeItem(registerLikeItemCommand))
                .isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 Item")
        void throwExceptionWhenNotFoundItem() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(itemRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> likeItemService.registerLikeItem(registerLikeItemCommand))
                .isInstanceOf(NotFoundItemException.class);
        }

        @Test
        @DisplayName("예외: 이미 찜한 Item")
        void throwExceptionWhenAlreadyLikedItem() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(item));
            given(likeItemRepository.findByUserAndItem(any(), any()))
                .willReturn(Optional.ofNullable(likeItem));

            //when
            //then
            assertThatThrownBy(() -> likeItemService.registerLikeItem(registerLikeItemCommand))
                .isInstanceOf(DuplicateLikeItemException.class);
        }
    }

    @Nested
    @DisplayName("deleteLikeItem 메서드 실행 시")
    class DeleteLikeItemTest {

        DeleteLikeItemCommand deleteLikeItemCommand = ItemFixture.deleteLikeItemCommand();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            ReflectionTestUtils.setField(user, "userId", 1L);

            given(likeItemRepository.findById(any())).willReturn(Optional.ofNullable(likeItem));

            //when
            likeItemService.deleteLikeItem(deleteLikeItemCommand);

            //then
            then(likeItemRepository).should().delete(any());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 LikeItem")
        void throwExceptionWhenNotFoundLikeItem() {
            //given
            given(likeItemRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> likeItemService.deleteLikeItem(deleteLikeItemCommand))
                .isInstanceOf(NotFoundLikeItemException.class);
        }

        @Test
        @DisplayName("예외: LikeItem의 User와 일치하지 않는 userId")
        void throwExceptionWhenNotEqualsUser() {
            //given
            Long userId = 1L;
            Long notEqualsUserId = 2L;
            ReflectionTestUtils.setField(user, "userId", userId);
            DeleteLikeItemCommand notEqualsUserIdCommand
                = new DeleteLikeItemCommand(notEqualsUserId, 1L);

            given(likeItemRepository.findById(any())).willReturn(Optional.ofNullable(likeItem));

            //when
            assertThatThrownBy(() -> likeItemService.deleteLikeItem(notEqualsUserIdCommand))
                .isInstanceOf(AuthorizationException.class);
        }
    }
}