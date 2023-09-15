# 🛒NaBMart

<img width="640" alt="스크린샷 2023-09-15 오후 1 28 28" src="https://github.com/prgrms-be-devcourse/BE-04-NaBMart/assets/97938489/0115f57b-e531-46fd-8a7a-7c77d3095f71">

> B마트 클론코딩 프로젝트<br>
> 1시간 이내에 소비자에게 상품을 배달하는 서비스
> 
> 프로젝트 기간 : 2023.08.29 ~ 2023.09.22
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
    <img src="https://img.shields.io/badge/Redis-D62124?style=flat-square&logo=Redis&logoColor=white">
</div>

<div>
    <img src="https://img.shields.io/badge/JUnit%205-25A162?style=flat-square&logo=JUnit&logoColor=white">
    <img src="https://img.shields.io/badge/RestDocs-2496ED?style=flat-square&logo=Swagger&logoColor=white">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white">
    <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=GitHub-Actions&logoColor=white">
</div>

### 툴
<div>
    <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white">
    <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=jirasoftware&logoColor=white">
    <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white">
    <img src="https://img.shields.io/badge/IntelliJ IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white">
    <img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=Apache-JMeter&logoColor=white">
</div>
</div>

## Architecture

## ERD

### Environment Variables
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
