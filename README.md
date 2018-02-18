### Project description
This project contains tests which placing bets with specific parameters. There are API tests.
In project there are using 5 different APIS: Accounts API, Bets API, Betslip API, Competitions API and Sessions API.

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
### Difficulties during develope test framework and what has been achieved:
During creating this test framework there were two difficulties:
- problems with connections to api
- lack of knowledge of the business domain
Despite these problems I created test framework with one specific test case of placing bet.

### Aspects to develope in next steps:
- 1. prepare next tests which consider different bet parameter (after better understand business domain):
- a. legType
- b. priceType
- c. corner cases for user balance
- d. corner cases for stakes form betslip
- e. freeBet
- 2. try to generate api objects using documentation (WADL format)

