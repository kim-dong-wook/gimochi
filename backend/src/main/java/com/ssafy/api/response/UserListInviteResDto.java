package com.ssafy.api.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserListInviteResDto {
    private String userNickname;

    private Long userId;

    private String userProfile;

    @Builder
    public UserListInviteResDto(String userNickname, Long userId, String userProfile) {
        this.userNickname = userNickname;
        this.userId = userId;
        this.userProfile = userProfile;
    }
}
