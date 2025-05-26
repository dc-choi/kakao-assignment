package kr.co.kakao.api.member.domain.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipationQualifications {
    // INFO: 첫 참여인 경우
    FIRST_TIME("FIRST_TIME"),
    // INFO: 이미 참여한 적이 있는 경우
    PARTICIPATION_COUNT("PARTICIPATION_COUNT"),
    // INFO: 특정 광고에 참여한 경우
    PARTICIPATED_AD_ID("PARTICIPATED_AD_ID");

    private final String description;
}
