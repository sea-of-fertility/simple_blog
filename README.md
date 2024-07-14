# 프로젝트 소개
회원가입 기능과 게시글을 올리는 사이트입니다.


# 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring_security-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white">
<img src="https://img.shields.io/badge/spring_JPA-6DB33F?style=for-the-badge&logo=springdatajpa&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/redis-red?style=for-the-badge&logo=redis&logoColor=white">

# 구현한 기능 
+ 게시글에 이미지를 첨부할 수 있다.
+ Offset 페이지 네이션을 방지하기 위해 스크롤 기능으로 구현했다.  
+ access token은 redis에 저장한다.
+ spring restDoc을 제작한다.
+ hateos을 이용한 

# 고민과 해결
1. test code
+ 고민)  [FileSystemStorageService](src/main/java/com/example/simple_blog/service/post/file/FileSystemStorageService.java)의 테스트 코드를 작성할 때 
+ [postService](src/main/java/com/example/simple_blog/service/post/PostService.java)의 save 메소드를 사용하는 부분이 있습다. FileSystemStorageService 테스트 코드의 결과가 postService 코드에 영향을 받아도 되는가에 대한 고민을 했습니다. 
+ 해결) 실제 이미지를 저장할 때 postService의 기능을 사용함으로 실제 작동과 유사한 결과를 보기 위해 FileSystemStorageService의 테스트 코드 postService의 save 메소드를 사용하게 했습니다.


# ERD
![erd](https://github.com/user-attachments/assets/8cadf863-c751-45c7-b796-b370a8857a5c)

# 개선 사항
+ [RefreshToken](src/main/java/com/example/simple_blog/config/schedul/RefreshTokenCheck.java)schedul의 학습을 위해 이러한 방식을 사용했지만 RefreshToken 또한 redis에 저장하는 방식을 사용하자.
+ member 도메인의  address는 회원의 실거주지와 E-mail 사이의 모호함을 줄 수 있음으로 EMail로 이름을 변경하는 것일 옳아 보인다.


# 앞으로의 기능 추가
+ spring-ai를 이용한 채팅구현
+ front-end와 통신을 통해 문제점 찾기