# Messenger Application

> 개발 기간 : 2022.10.26 ~ 2023.01.10 (2개월)

## 개발팀 소개

|      김동진       |          민석홍         |       최민재         |                                                                                                               
| :------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | 
|   <img width="160px" src="https://avatars.githubusercontent.com/u/82895809?v=4" />    |                      <img width="160px" src="https://avatars.githubusercontent.com/u/67637716?v=4" />    |                   <img width="160px" src="https://avatars.githubusercontent.com/u/92290312?v=4"/>   |
|   [@Dongjin0224](https://github.com/Dongjin0224)   |    [@shmin7777](https://github.com/shmin7777)  | [@alswo1212](https://github.com/alswo1212)  |
<br>
## 프로젝트 소개

Non-Blocking과 MQ 숙련도 향상을 위해 진행하였으며, 저희
서비스에 가입한 사용자는 1:1 채팅이나 그룹 채팅을 이용하여 다른
사람들과 빠르게 채팅을 주고 받을 수 있습니다.
<br>
## CI/CD Process
![image](https://user-images.githubusercontent.com/67637716/229171222-9f33422b-2f08-4500-aee5-d5c091edc9d3.png)  
<br>
## Architecture
![image](https://user-images.githubusercontent.com/67637716/229171296-5bcc1ac0-ab7c-41d9-a4c1-c8167e724fd5.png)
<br>


## Stacks 🐈
### Develop
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
<img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=ApacheKafka&logoColor=white">
<img src="https://img.shields.io/badge/socket.io-010101?style=for-the-badge&logo=socket.io&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/Google Cloud-4285F4?style=for-the-badge&logo=GoogleCloud&logoColor=white">
<img src="https://img.shields.io/badge/ReactiveX-B7178C?style=for-the-badge&logo=ReactiveX&logoColor=white">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">

## Communication
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"> <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white">



---
## 화면 구성 📺
| 메인 페이지  |  친구 추가 페이지   |
| :-------------------------------------------: | :------------: |
|  ![image](https://user-images.githubusercontent.com/67637716/229178404-da08a913-5806-470f-9286-729d4ef86d19.png) | ![image](https://user-images.githubusercontent.com/67637716/229178278-0760e8b6-db80-4746-9d12-acfbf180a011.png)|  
| 채팅방 만들기   |  로그인   |  
| ![image](https://user-images.githubusercontent.com/67637716/229178596-6070fc60-e16e-4261-8ef1-151041cf6c63.png)   | ![image](https://user-images.githubusercontent.com/67637716/229178832-4150a0ff-69ce-46bd-ab28-4f4efcce4c01.png)     |

---
## 주요 기능 📦

### ⭐️ 채팅 기능
- File 포함 Message 주고 받을 수 있음
- 현재 들어와 있는 채팅방에서 상대방이 입력 중일 때 표시
- 채팅방 목록이 실시간으로 변경
- File 클릭 시 다운로드

### ⭐️ 친구 추가
- 친구 이름 또는 이메일 검색 시 해당 유저 조회
- 친구 목록 조회
- 친구 클릭시 대화방 생성

### ⭐️ 채팅방 만들기
- 1:1 채팅, 그룹 채팅방 생성 가능
- 채팅방 목록 조회

---

## 디렉토리 구조
#### Client
```bash
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── shell
│   ├── Dockerfile
│   ├── messenger_client.sh
│   ├── messenger_server.sh
│   └── messenger_server.sh.bak
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── teamride
    │   │           └── messenger
    │   │               └── client
    │   │                   ├── MessengerClientApplication.java
    │   │                   ├── config
    │   │                   │   ├── ApplicationContextProvider.java
    │   │                   │   ├── ClientConfig.java
    │   │                   │   ├── Constants.java
    │   │                   │   ├── KafkaAdminClientConfig.java
    │   │                   │   ├── KafkaConstants.java
    │   │                   │   ├── KafkaConsumerConfig.java
    │   │                   │   ├── KafkaProducerConfig.java
    │   │                   │   ├── WebClientConfig.java
    │   │                   │   ├── WebConfig.java
    │   │                   │   └── WebSocketConfig.java
    │   │                   ├── controller
    │   │                   │   ├── KakaoLoginController.java
    │   │                   │   ├── NaverLoginController.java
    │   │                   │   ├── RoomController.java
    │   │                   │   ├── ServerConnectController.java
    │   │                   │   ├── StompChatController.java
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── ChatMessageDTO.java
    │   │                   │   ├── ChatRoomDTO.java
    │   │                   │   ├── FriendDTO.java
    │   │                   │   ├── FriendInfoDTO.java
    │   │                   │   └── UserDTO.java
    │   │                   ├── interceptor
    │   │                   │   └── LoginCheckInterceptor.java
    │   │                   ├── repository
    │   │                   │   └── ChatRoomRepository.java
    │   │                   ├── service
    │   │                   │   ├── MailService.java
    │   │                   │   ├── StompChatService.java
    │   │                   │   └── UserService.java
    │   │                   └── utils
    │   │                       ├── BeanUtils.java
    │   │                       └── RestResponse.java
    └── resources
    │       ├── application.yml
 

```

#### Server
```bash
─ build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── shell
│   ├── Dockerfile
│   ├── messenger_server.sh
│   └── messenger_server.sh.bak
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── teamride
    │   │           └── messenger
    │   │               └── server
    │   │                   ├── MessengerServerApplication.java
    │   │                   ├── config
    │   │                   │   ├── KafkaConstants.java
    │   │                   │   ├── KafkaConsumerConfig.java
    │   │                   │   ├── KafkaProducerConfig.java
    │   │                   │   └── WebConfig.java
    │   │                   ├── controller
    │   │                   │   ├── ChatController.java
    │   │                   │   ├── KafkaController.java
    │   │                   │   ├── MailService.java
    │   │                   │   ├── TestController.java
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── ChatMessageDTO.java
    │   │                   │   ├── ChatRoomDTO.java
    │   │                   │   ├── FriendDTO.java
    │   │                   │   ├── FriendInfoDTO.java
    │   │                   │   ├── SaveUserDTO.java
    │   │                   │   └── UserDTO.java
    │   │                   ├── entity
    │   │                   │   ├── ChatMessageEntity.java
    │   │                   │   ├── ChatRoomEntity.java
    │   │                   │   ├── FriendEntity.java
    │   │                   │   └── UserEntity.java
    │   │                   ├── repository
    │   │                   │   ├── ChatMessageRepository.java
    │   │                   │   ├── ChatRoomRepository.java
    │   │                   │   ├── FriendRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   ├── service
    │   │                   │   ├── ChatService.java
    │   │                   │   └── UserService.java
    │   │                   └── util
    │   │                       └── RestResponse.java
    │   └── resources
    │       ├── application-prod.yml
    │       ├── application.yml


```

