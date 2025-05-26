package kr.co.kakao.util;

import kr.co.kakao.api.ad.domain.entity.Ad;

import java.time.LocalDate;
import java.util.List;

public class MockUtil {
    public static List<Ad> FindAllAd() {
        return List.of(
                Ad.builder()
                        .name("광고 제목1")
                        .rewardAmount(100)
                        .participationCount(0)
                        .totalPoint(1000)
                        .description("설명1")
                        .imageUrl("https://example.com/image1.jpg")
                        .startedAt(LocalDate.of(2025, 5, 1))
                        .endedAt(LocalDate.of(2025, 6, 1))
                        .participationQualifications("전체")
                        .build(),

                Ad.builder()
                        .name("광고 제목2")
                        .rewardAmount(200)
                        .participationCount(1)
                        .totalPoint(2000)
                        .description("설명2")
                        .imageUrl("https://example.com/image2.jpg")
                        .startedAt(LocalDate.of(2025, 5, 2))
                        .endedAt(LocalDate.of(2025, 6, 2))
                        .participationQualifications("10대")
                        .build(),

                Ad.builder()
                        .name("광고 제목3")
                        .rewardAmount(300)
                        .participationCount(2)
                        .totalPoint(3000)
                        .description("설명3")
                        .imageUrl("https://example.com/image3.jpg")
                        .startedAt(LocalDate.of(2025, 5, 3))
                        .endedAt(LocalDate.of(2025, 6, 3))
                        .participationQualifications("20대")
                        .build(),

                Ad.builder()
                        .name("광고 제목4")
                        .rewardAmount(400)
                        .participationCount(3)
                        .totalPoint(4000)
                        .description("설명4")
                        .imageUrl("https://example.com/image4.jpg")
                        .startedAt(LocalDate.of(2025, 5, 4))
                        .endedAt(LocalDate.of(2025, 6, 4))
                        .participationQualifications("30대")
                        .build(),

                Ad.builder()
                        .name("광고 제목5")
                        .rewardAmount(500)
                        .participationCount(4)
                        .totalPoint(5000)
                        .description("설명5")
                        .imageUrl("https://example.com/image5.jpg")
                        .startedAt(LocalDate.of(2025, 5, 5))
                        .endedAt(LocalDate.of(2025, 6, 5))
                        .participationQualifications("40대")
                        .build(),

                Ad.builder()
                        .name("광고 제목6")
                        .rewardAmount(600)
                        .participationCount(5)
                        .totalPoint(6000)
                        .description("설명6")
                        .imageUrl("https://example.com/image6.jpg")
                        .startedAt(LocalDate.of(2025, 5, 6))
                        .endedAt(LocalDate.of(2025, 6, 6))
                        .participationQualifications("50대")
                        .build(),

                Ad.builder()
                        .name("광고 제목7")
                        .rewardAmount(700)
                        .participationCount(6)
                        .totalPoint(7000)
                        .description("설명7")
                        .imageUrl("https://example.com/image7.jpg")
                        .startedAt(LocalDate.of(2025, 5, 7))
                        .endedAt(LocalDate.of(2025, 6, 7))
                        .participationQualifications("여성")
                        .build(),

                Ad.builder()
                        .name("광고 제목8")
                        .rewardAmount(800)
                        .participationCount(7)
                        .totalPoint(8000)
                        .description("설명8")
                        .imageUrl("https://example.com/image8.jpg")
                        .startedAt(LocalDate.of(2025, 5, 8))
                        .endedAt(LocalDate.of(2025, 6, 8))
                        .participationQualifications("남성")
                        .build(),

                Ad.builder()
                        .name("광고 제목9")
                        .rewardAmount(900)
                        .participationCount(8)
                        .totalPoint(9000)
                        .description("설명9")
                        .imageUrl("https://example.com/image9.jpg")
                        .startedAt(LocalDate.of(2025, 5, 9))
                        .endedAt(LocalDate.of(2025, 6, 9))
                        .participationQualifications("임산부")
                        .build(),

                Ad.builder()
                        .name("광고 제목10")
                        .rewardAmount(1000)
                        .participationCount(9)
                        .totalPoint(10000)
                        .description("설명10")
                        .imageUrl("https://example.com/image10.jpg")
                        .startedAt(LocalDate.of(2025, 5, 10))
                        .endedAt(LocalDate.of(2025, 6, 10))
                        .participationQualifications("누구나")
                        .build()
        );
    }
}
