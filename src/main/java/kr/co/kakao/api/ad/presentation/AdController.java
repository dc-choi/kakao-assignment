package kr.co.kakao.api.ad.presentation;

import kr.co.kakao.api.ad.application.AdService;
import kr.co.kakao.api.ad.domain.dto.request.CreateAdRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateAdResponse;
import kr.co.kakao.api.ad.domain.dto.response.FindAllAdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

    @PostMapping
    public ResponseEntity<CreateAdResponse> create(@RequestBody CreateAdRequest createAdRequest) {
        URI location = URI.create("/api/v1/ads");

        return ResponseEntity.created(location).body(adService.createAd(createAdRequest));
    }

    @GetMapping
    public ResponseEntity<List<FindAllAdResponse>> findAll() {
        return ResponseEntity.ok(adService.findAllAds());
    }
}
