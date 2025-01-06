# 서비스 소개

- 직장인을 대상으로 한 맛집 추천 피드 시스템이에요.
- 점심 시간에 몰리는 트래픽을 효율적으로 분산하고 처리하는 아키텍처를 설계하고 구현하였습니다.

# 서비스 주요 기능

- Push & Pull 모델의 피드 시스템
- SQS & Lambda 기반의 서버리스 팔로우 시스템
- EventBridge & Redis & Lambda 기반의 DB 부하 분산 아키텍처

# 기술 스택
- Tech Stack
  - Java, Spring Boot, MySQL, JPA, Redis
- Infra
  - AWS EC2, AWS ALB, AWS RDS, AWS ElasticCache, AWS SQS, AWS Lambda, AWS EventBridge
- DevOps
  - Docker, Github Actions
 
# 아키텍처

<img src="https://github.com/user-attachments/assets/7c18e25c-a49e-4de1-8c36-814c3a58ddee" alt="image" width="800"> <br><br>

- **다중 가용영역(Multi-AZ)**: 여러 가용 영역에 리소스를 분산 배치하여 가용성을 높이고 단일 장애 지점(Single Point of Failure, SPOF)을 제거합니다. <br>
- **Scale-Out**: Auto Scaling과 ELB로 수평적으로 확장한 서버들에게 트래픽을 분배합니다. <br>
- **Master-Slave**: RDS를 쓰기 전용 Master와 읽기 전용 Slave로 분리해 조회 성능을 최적화합니다.


<br>


# 구현 및 문제 해결 과정

<img src="https://github.com/user-attachments/assets/0b39ac89-02c0-4b4e-b4dd-24598b23d233" alt="image" width="800">


**1. SQS & Lambda 기반의 서버리스 팔로우 시스템**

- 점심 시간에 스파크 트래픽이 발생한다는 가정 하에, EC2 부하 감소를 위해 팔로우 API를 서버리스로 구현하였습니다.
- Lambda는 스크립트 언어에 적합하지만, 숙련도 이슈로 Java를 사용해야 했습니다. 따라서 다음과 같은 과정으로 구현하였습니다.
  1.	Spring Boot 애플리케이션에 Function을 정의합니다.
  2.	FunctionInvoker를 확장하여 AWS Lambda가 이벤트를 처리할 때, Spring Cloud Function을 호출해줄 핸들러로 설정합니다.
  3.	AWS Lambda에 배포하여 Spring Cloud Function이 이벤트를 처리하도록 합니다.


**2. EventBridge & Redis & Lambda 기반의 DB 부하 분산 아키텍처**

- DB 부하 감소를 위해 Redis에 데이터를 저장한 뒤, 사용자 이용이 적은 시간에 EventBridge로 Redis의 데이터를 RDBMS에 마이그레이션 하였습니다.
- 동시성 문제를 해결하기 위해 분산락 도입이 요구될 것으로 예상됩니다.






