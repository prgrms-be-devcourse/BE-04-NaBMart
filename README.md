# 🛒NaBMart

<img width="1000" height="300" alt="스크린샷 2023-09-15 오후 1 28 28" src="https://github.com/prgrms-be-devcourse/BE-04-NaBMart/assets/70627982/b6ddf10b-afa4-4fcc-92fd-b745b77af15b">



> B마트 클론코딩 프로젝트<br>
> 1시간 이내에 소비자에게 상품을 배달하는 서비스
> 
> 프로젝트 기간 : 2023.08.29 ~ 2023.09.22

## Tech Stack
<div align="left">
<div>
    <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white">
    <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=Spring-Boot&logoColor=white">
</div>

<div>
    <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=Spring-Data-JPA&logoColor=white">
    <img src="https://img.shields.io/badge/QueryDSL-009630?style=flat-square&logo=Gradle&logoColor=white">
    <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring-Security&logoColor=white">
    <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON-Web-Tokens&logoColor=white">
    <img src="https://img.shields.io/badge/OAuth%202.0-3EA0F6?style=flat-square&logo=OAuth&logoColor=white">
</div>

<div>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white">
    <img src="https://img.shields.io/badge/H2-000000?style=flat-square&logo=h2&logoColor=white">
    <img src="https://img.shields.io/badge/Redis-D62124?style=flat-square&logo=Redis&logoColor=white">
</div>

<div>
    <img src="https://img.shields.io/badge/JUnit%205-25A162?style=flat-square&logo=JUnit&logoColor=white">
    <img src="https://img.shields.io/badge/RestDocs-2496ED?style=flat-square&logo=Swagger&logoColor=white">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white">
    <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=GitHub-Actions&logoColor=white">
</div>

### Tool
<div>
    <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white">
    <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=jirasoftware&logoColor=white">
    <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white">
    <img src="https://img.shields.io/badge/IntelliJ IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white">
    <img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=Apache-JMeter&logoColor=white">
</div>
</div>

## Architecture
<img width="633" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-NaBMart/assets/70627982/60f23ccd-7940-4b9f-a177-941bbf35b60e">



## ERD
![B마트_ERD](https://github.com/prgrms-be-devcourse/BE-04-NaBMart/assets/70627982/89ddfea4-be0c-470f-be55-3fc11aff9679)




## Environment Variables
```
CLIENT_SECRET=;
EXPIRY_SECONDS=60;
ISSUER=;

KAKAO_CLIENT_ID=;
KAKAO_CLIENT_SECRET=;
NAVER_CLIENT_ID=;
NAVER_CLIENT_SECRET=;
REDIRECT_URI=http://localhost:8080/login/oauth2/code/{registrationId};

REDIS_HOST=localhost;
REDIS_PORT=6379;

TOSS_FAIL_URL=http://localhost:8080/api/v1/pays/toss/fail;
TOSS_SUCCESS_URL=http://localhost:8080/api/v1/pays/toss/success;
TOSS_SECRET_KEY=
```

[Additional Local or Docker Environment Variables](https://mellow-shadow-fed.notion.site/4ac5c19cc7254fcc8364bfa5f3653bca?p=d19f142187be44fe8a4e049afdae6035&pm=s)

## navy Team

<table>
    <tr align="center">
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
    </tr>
    <tr align="center">
        <td><a href="https://github.com/hseong3243">박혜성</a></td>
        <td><a href="https://github.com/seongHyun-Min">민승현</a></td>
        <td><a href="https://github.com/bjo6300">배준일</a></td>
        <td><a href="https://github.com/Seongju-Lee">이성주</a></td>
        <td><a href="https://github.com/funnysunny08">전선희</a></td>
        <td><a href="https://github.com/pushedrumex">김민정</a></td>
    </tr>
    <tr align="center">
        <td>
            <img src="https://github.com/hseong3243.png?size=100">
        </td>
        <td>
            <img src="https://github.com/seongHyun-Min.png?size=100">
        </td>
        <td>
            <img src="https://github.com/bjo6300.png?size=100">
        </td>
        <td>
            <img src="https://github.com/Seongju-Lee.png?size=100">
        </td>
        <td>
            <img src="https://github.com/funnysunny08.png?size=100">
        </td>
        <td>
            <img src="https://github.com/pushedrumex.png?size=100">
        </td>
    </tr>
</table>


## API 명세서
[API DOCS🗂](https://www.notion.so/e81ef4dd063149e0a161cb1119a9e602?v=1199d4e7df0d4277882ac007574b428b)

## 이슈/해결 아카이빙
[이슈/해결 아카이빙🔨](https://backend-devcourse.notion.site/9e8698fc02064844ae7f252482a4bc37?pvs=4)  
- [무한 스크롤 개선기](https://backend-devcourse.notion.site/1bcabbf25fcd46aa97acdd2039565a62?pvs=4)  
- [낙관적 락과 동시성 테스트 하기](https://backend-devcourse.notion.site/2c8a01ff213b44db93325e78eeea5e28?pvs=4)  
- [Toss Pay 연동하기](https://backend-devcourse.notion.site/Toss-Pay-fcedbe30842042ac94d23d983da9a3b2?pvs=4)  
- [리뷰에서 활용하는 Redis](https://www.notion.so/backend-devcourse/Redis-bdc58dba600c4084b0ab77dcf0a82238)  
- [Redis testContainer 설정 방법](https://backend-devcourse.notion.site/Redis-testContainer-68407c4ce52549daa65526bade155c9e?pvs=4)  
- [트러블슈팅 in Redis의 List 자료구조](https://backend-devcourse.notion.site/in-Redis-List-1b5bc99110b249048767cb15530ae7de?pvs=4)  
- [분산 환경에서 스케줄링한 작업이 한 번만 실행되도록 보장하기](https://backend-devcourse.notion.site/74c17904c5a4497bbf64ebde5578339d?pvs=4)  
- [MySql 동시성 문제해결 낙관적 락, 비관적 락??](https://backend-devcourse.notion.site/MySql-ced47b1f4e274ea39a994ce31de3ff16?pvs=4)  
- [QueryDSL, OrderSpecifier을 이용하여 정렬하기](https://backend-devcourse.notion.site/QueryDSL-OrderSpecifier-0a3c3bd2046141d7bca1ddd6379fc1d2?pvs=4)  
