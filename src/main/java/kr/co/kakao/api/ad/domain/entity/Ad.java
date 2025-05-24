package kr.co.kakao.api.ad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.co.kakao.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ads")
public class Ad extends BaseEntity {
    @Column(name = "name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    @Column(name = "reward_amount", columnDefinition = "INT", nullable = false)
    private int rewardAmount;

    @Column(name = "participation_count", columnDefinition = "INT", nullable = false)
    private int participationCount;

    @Column(name = "total_point", columnDefinition = "INT", nullable = false)
    private int totalPoint;

    @Column(name = "description", columnDefinition = "VARCHAR(255)", nullable = false)
    private String description;

    @Column(name = "image_url", columnDefinition = "VARCHAR(255)", nullable = false)
    private String imageUrl;

    @Column(name = "started_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDate startedAt;

    @Column(name = "ended_at", columnDefinition = "VARCHAR(20)", nullable = false)
    private LocalDate endedAt;

    @Column(name = "participation_qualifications", columnDefinition = "VARCHAR(20)")
    private String participationQualifications;
}
