# kakao-assignment
경력 카카오 채용 과제

# 기술 스택
- Java 21, Spring Boot 3.5.0, JPA
- MySQL 8.0.34, Redis:7-alpine3.17
- JUnit 5, Mockito

# 시스템 설계
### 비즈니스 요구사항의 경우 "광고에 참여하면 보상을 주는 시스템"입니다.
제약 사항은 다음과 같습니다.

- 유니크한 광고 ID를 생성하여 저장합니다.
- 광고명은 중복될 수 없습니다.
- 조회 시점에 참여가 가능한 광고만 노출합니다.
  - 광고 참여 가능 횟수가 소진관 광고는 노출되지 않습니다.
  - 광고 노출 기간이 아닌 광고는 노출되지 않습니다.
- 한번에 최대 10개의 광고만 노출합니다.
- 광고 참여 시 적립 액수가 높은 순으로 조회됩니다.

### 다중 서버 환경에서 개발을 전제로 하였습니다.
- Redis 분산락을 활용한 선착순 제어를 위해 설계하였습니다.
- 광고에 참여하는 하는 경우 참여 횟수 감소하는 부분에 적용하였습니다.
- race condition을 방지하기 위해 Redis의 분산락을 사용하였습니다.
- 동일 광고에 대해 여러 유저가 동시에 참여할 때 중복 참여나 초과 참여 방지.
- Redisson의 RLock을 활용하여 임계영역 보호. 

### JPA 기반 도메인 설계
  - Member: 광고에 참여하는 사용자 (추후에 추후 사용자 포인트 시스템이나 등급제가 붙을 것을 고려하여 설계)
  - Ad: 광고 정보
  - AdParticipation: 광고 참여 정보

### 포인트 적립 API 호출 부분 인터페이스화 (mockable)
- 추후 외부 API 호출 실패시 해당 부분의 변경이 용이하도록 인터페이스로 분리.

### 테스트 코드 작성
- JUnit5, Mockito를 활용하여 테스트 코드 작성.
- Mock 객체를 활용하여 비즈니스 로직 검증에 집중하도록 함.

# 세부 구현 사항 (API 명세)
### 1. 광고 등록 API
광고 정보를 담아 POST 요청을 보내면 새로운 광고가 등록됩니다. 광고 등록 시 참여 자격 조건을 설정할 수 있습니다.

request:
```http request
POST /v1/ads
Content-Type: application/json

{
  "name": "광고명",
  "rewardAmount": 1000,
  "participationCount": 100,
  "description": "광고 설명",
  "imageUrl": "https://example.com/image.jpg",
  "startedAt": "2023-10-01T00:00:00",
  "endedAt": "2023-10-31T23:59:59",
  "participationQualifications": ["FIRST_TIME", "PARTICIPATION_COUNT", "PARTICIPATED_AD_ID"]
}
```

200 response:
``` http response
HTTP/1.1 201 Created
Content-Type: application/json
Location: /v1/ads/{adId}

{
  "name": "광고명",
  "rewardAmount": 1000,
  "participationCount": 100,
  "description": "광고 설명",
  "imageUrl": "https://example.com/image.jpg",
  "startedAt": "2023-10-01T00:00:00",
  "endedAt": "2023-10-31T23:59:59",
  "participationQualifications": ["FIRST_TIME", "PARTICIPATION_COUNT", "PARTICIPATED_AD_ID"]
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "요청한 값이 유효하지 않습니다",
  "path": "/v1/ads",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

### 2. 광고 조회 API
광고 목록을 조회합니다. 참여 가능한 광고만 노출되며, 최대 10개까지 조회됩니다. 참여 가능 여부는 참여 횟수와 기간에 따라 결정됩니다.

request:
```http request
GET /v1/ads
```

200 response:
``` http response
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "광고명",
    "rewardAmount": 1000,
    "participationCount": 100,
    "description": "광고 설명",
    "imageUrl": "https://example.com/image.jpg",
    "startedAt": "2023-10-01T00:00:00",
    "endedAt": "2023-10-31T23:59:59",
    "participationQualifications": ["FIRST_TIME", "PARTICIPATION_COUNT", "PARTICIPATED_AD_ID"]
  },
  {
    "id": 2,
    "name": "또 다른 광고",
    "rewardAmount": 2000,
    "participationCount": 50,
    "description": "또 다른 광고 설명",
    "imageUrl": "https://example.com/another-image.jpg",
    "startedAt": "2023-10-05T00:00:00",
    "endedAt": "2023-10-25T23:59:59", 
    "participationQualifications": ["FIRST_TIME"]
  }
  // ... 최대 10개까지 광고가 조회됩니다
]
```

### 3. 광고 참여 API
광고에 참여합니다. 참여 시 참여 자격 조건을 확인하고, 참여 횟수를 감소시킵니다. 참여가 성공하면 포인트를 적립합니다.

request:
```http request
POST /v1/ads/participate
Content-Type: application/json

{
  "adId": 1,
  "memberId": 12345
}
```

200 response:
``` http response
HTTP/1.1 200 OK
Content-Type: application/json

{
  "adId": 1,
  "memberId": 12345
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "존재하지 않는 회원입니다.",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "존재하지 않는 광고입니다.",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "해당 광고에 참여할 수 있는 횟수가 부족합니다.",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "해당 광고에 참여할 수 있는 횟수가 부족합니다.",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

500 response:
``` http response
HTTP/1.1 500 INTERNAL_SERVER_ERROR
Content-Type: application/json

{
  "message": "락 획득 실패.",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 500
}
```

500 response:
``` http response
HTTP/1.1 500 INTERNAL_SERVER_ERROR
Content-Type: application/json

{
  "message": "락 획득 중 인터럽트 발생",
  "path": "/v1/ads/participate",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 500
}
```

### 4. 광고 참여 이력 조회 API
광고 참여 이력을 조회합니다. 특정 회원의 광고 참여 이력을 조회할 수 있습니다. 광고 참여 시각이 오래된 순으로 정렬됩니다.

한번에 최대 50 개의 이력을 조회할 수 있습니다. 페이지네이션이 적용되었습니다.

request:
```http request
GET /v1/members/{memberId}/participations?page=0&size=50&startedAt=2023-10-01&endedAt=2023-10-31
```

200 response:
``` http response
HTTP/1.1 200 OK

{
    "content": [
        {
            "createdAt": "2023-10-01T12:00:00",
            "memberId": 12345,
            "adId": 1,
            "adName": "광고명",
            "rewardAmount": 1000
        },
        {
            "createdAt": "2023-10-02T12:00:00",
            "memberId": 12345,
            "adId": 2,
            "adName": "광고명2",
            "rewardAmount": 10000
        },
        ...
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 50,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "size": 50,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "numberOfElements": 0,
    "empty": true
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "시작 날짜는 종료 날짜보다 이전이어야 합니다.",
  "path": "/v1/members/{memberId}/participations",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```

400 response:
``` http response
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "존재하지 않는 회원입니다.",
  "path": "/v1/members/{memberId}/participations",
  "timestamp": "2023-06-10T15:30:45.123Z",
  "status": 400
}
```