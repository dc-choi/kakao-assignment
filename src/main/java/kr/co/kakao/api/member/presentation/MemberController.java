package kr.co.kakao.api.member.presentation;

import kr.co.kakao.api.ad.application.AdParticipationService;
import kr.co.kakao.api.ad.domain.dto.response.FindAllParticipationResponse;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final AdParticipationService adParticipationService;

    @GetMapping("/{memberId}/participation")
    public ResponseEntity<List<FindAllParticipationResponse>> findAllParticipation(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam(name = "startedAt") LocalDate startedAt,
            @RequestParam(name = "endedAt") LocalDate endedAt
    ) {
        // INFO: spring에서 페이지는 0부터 시작하므로 1을 빼줌
        // 프론트엔드측에서 사용하기 편하도록 1부터 시작하는 페이지 넘버를 사용하도록 함
        int parsePage = page - 1;
        // INFO: 최대 페이지 크기를 50으로 제한
        int parseSize = size > 50 ? 50 : size;

        Pageable pageable = PageRequest.of(parsePage, parseSize);
        return ResponseEntity.ok(adParticipationService.findAllParticipation(memberId, pageable, startedAt, endedAt));
    }
}
