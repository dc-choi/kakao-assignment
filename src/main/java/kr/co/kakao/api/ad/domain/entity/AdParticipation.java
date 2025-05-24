package kr.co.kakao.api.ad.domain.entity;

import jakarta.persistence.*;
import kr.co.kakao.api.member.domain.entity.Member;
import kr.co.kakao.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ad_participations")
public class AdParticipation extends BaseEntity {
    @Column(name = "name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    @Column(name = "reward_amount", columnDefinition = "INT", nullable = false)
    private int rewardAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(name = "fk_ad_participations_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(name = "fk_ad_participations_ad"))
    private Ad ad;
}
