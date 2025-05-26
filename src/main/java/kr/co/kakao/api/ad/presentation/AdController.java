package kr.co.kakao.api.ad.presentation;

import kr.co.kakao.api.ad.application.AdService;
import kr.co.kakao.api.ad.domain.dto.request.CreateAdRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateAdResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

    @PostMapping
    public ResponseEntity<CreateAdResponse> create(@RequestBody CreateAdRequest createAdRequest) {
        URI location = URI.create("/api/v1/ads/"); // Assuming name is unique for simplicity

        return ResponseEntity.created(location).body(adService.createAd(createAdRequest)); // Placeholder response, replace with actual logic
    }
}
