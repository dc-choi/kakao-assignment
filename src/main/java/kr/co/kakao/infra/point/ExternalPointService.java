package kr.co.kakao.infra.point;

/**
 * Mock interface
 */
public interface ExternalPointService {
    void earnPoints(Long memberId, int amount);
}
