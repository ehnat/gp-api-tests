### Project description
This project contains tests which placing bets with specific parameters. There are API tests.
In project there are using 5 different api: Accounts API, Bets API, Betslips API, Competitions API and Sessions API.

### Technical description
- **Used technologies:** Java 8, Maven (preferred v.3.3.9)
- **Used frameworks/libraries:** Rest-Assured, TestNG, AssertJ, Lombok, Jackson

### Necessary to download/install to run the project
1. install Java 8
2. install Maven

### How to run test
1. clone this repo
3. enter to the folder **gp-api-tests** (place where file 'pom.xml' is located)
4. run command from command line:

  ```sh
    mvn clean test
  ```
### Difficulties during developing test framework and what has been achieved:
During creating this test framework there were two difficulties:
- problems with connections to api
- lack of knowledge of the business domain
Despite these problems I created test framework with two specific test cases of placing bet.

### Aspects to develop in next steps:
1. Prepare next tests which consider different bet parameters (after better understanding business domain):
    - legType
    - priceType
    - corner cases for user balance
    - corner cases for stakes from betslip
    - freeBet
2. Generate api objects from documentation (WADL) instead creating them in the code

