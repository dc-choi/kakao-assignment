package kr.co.kakao.api.ad.application;

import kr.co.kakao.api.ad.domain.dto.request.CreateAdRequest;
import kr.co.kakao.api.ad.domain.dto.response.CreateAdResponse;
import kr.co.kakao.api.ad.domain.dto.response.FindAllAdResponse;
import kr.co.kakao.api.ad.domain.entity.Ad;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import kr.co.kakao.global.lock.DistributedLock;
import kr.co.kakao.infra.persistence.ad.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;

    @DistributedLock(key = "#createAdRequest.name")
    @Transactional
    public CreateAdResponse createAd(CreateAdRequest createAdRequest) {
        Ad vaildAd = adRepository.findByName(createAdRequest.name());
        if (vaildAd != null) {
            throw new BusinessException(FailHttpMessage.CONFLICT);
        }

        Ad adEntity = CreateAdRequest.toEntity(createAdRequest);

        Ad savedAd = adRepository.save(adEntity);

        return CreateAdResponse.toDto(savedAd);
    }

    public List<FindAllAdResponse> findAllAds() {
        return adRepository.findTop10ValidAds(LocalDate.now())
                .stream()
                .map(FindAllAdResponse::toDto)
                .toList();
    }
}
