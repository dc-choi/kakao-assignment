package kr.co.kakao.global.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessHttpMessage {
    // 200
    OK(HttpStatus.OK, "성공"),
    CREATED(HttpStatus.CREATED, "리소스가 생성되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "성공하였지만, 리소스가 없습니다."),

    // 300
    MOVED_PERMANENTLY(HttpStatus.MOVED_PERMANENTLY, "영구적으로 이동합니다."),
    FOUND(HttpStatus.FOUND, "다른 URI에서 리소스를 찾았습니다."),
    NOT_MODIFIED(HttpStatus.NOT_MODIFIED, "캐시를 사용하세요."),
    TEMPORARY_REDIRECT(HttpStatus.TEMPORARY_REDIRECT, "다른 URI에서 리소스를 찾았고 본문도 유지됩니다."),
    PERMANENT_REDIRECT(HttpStatus.PERMANENT_REDIRECT, "영구적으로 이동하고 본문도 유지됩니다.");

    private final HttpStatus status;
    private final String message;
}
