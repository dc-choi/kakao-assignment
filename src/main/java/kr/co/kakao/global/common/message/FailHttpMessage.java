package kr.co.kakao.global.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FailHttpMessage {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    BAD_REQUEST_SUBSCRIBE(HttpStatus.BAD_REQUEST, "잘못된 구독 변경입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값 입니다."),
    INVALID_INPUT_VALUE_CHANNEL(HttpStatus.BAD_REQUEST, "잘못된 채널 입력값 입니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    NOT_NULL(HttpStatus.BAD_REQUEST, "null 값은 입력할 수 없습니다."),
    NOT_BLANK(HttpStatus.BAD_REQUEST, "빈 값은 입력할 수 없습니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
    INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "계정정보가 일치하지 않습니다."),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 접근입니다."),
    FORBIDDEN_CHANNEL(HttpStatus.FORBIDDEN, "권한이 없는 채널입니다."),
    DEACTIVATE_USER(HttpStatus.FORBIDDEN, "비활성화 상태 계정입니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_FOUND_SUBSCRIBE(HttpStatus.NOT_FOUND, "존재하지 않는 구독 정보입니다."),

    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP METHOD 입니다."),

    // 409
    CONFLICT(HttpStatus.CONFLICT, "이미 리소스가 존재합니다."),

    // 500 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    REDIS_LOCK_CONFLICT(HttpStatus.INTERNAL_SERVER_ERROR, "락 획득 실패."),
    REDIS_LOCK_INTERRUPT(HttpStatus.INTERNAL_SERVER_ERROR, "락 획득 중 인터럽트 발생");

    private final HttpStatus status;
    private final String message;
}
