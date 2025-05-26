package kr.co.kakao.infra.point;

import org.springframework.stereotype.Service;

/**
 * Mock Service
 */
@Service
public class MockExternalPointService implements ExternalPointService {
    @Override
    public void earnPoints(Long memberId, int amount) {}
}
