# selenium-sample
This repository is intended to help people get started with Selenium automation.
The source code included in this project has many comments explaining how to use this framework and how you can scale it up. 

## Tools Used
- [Gradle](https://gradle.org/)
- [TestNG](https://testng.org)
- [Selenium](https://www.selenium.dev/)

## Recommended Guidelines
- [Selenium's Official Test Practices](https://www.selenium.dev/documentation/test_practices/)
- It is a good practice to check first if the Grid is up and ready to receive requests. See [Waiting for the Grid to be ready](https://github.com/SeleniumHQ/docker-selenium#waiting-for-the-grid-to-be-ready) for more info.

## Project Setup
### Prerequisites
1. Ensure you have JDK 11 installed. This project uses Java 11 features so you'll at least need that installed to build this project.
2. Ensure you have the [ChromeDriver](https://chromedriver.chromium.org/downloads) installed at the appropriate path.
   1. See the `src/test/resources/test.properties` file to see where this project expects it. 
   2. Make sure the ChromeDriver supports the version of Chrome you have installed.
3. Optionally you can install Gradle but this isn't required.

### Test Execution
Depending on your Operating System, the execution command will be slightly different.
* **Windows:** `gradlew.bat test`
* **Mac/Linux:** `./gradlew test`

If you wish to run the test on the Selenium Grid instead, you'll need to pass the grid flag.
You can view the test while it's running via [this link](https://selenium-grid.np-shared.shwaks.com).
Make sure the Selenium grid instance corresponds with the `seleniumGrid` property defined in `src/test/resources/test.properties`.
* **Windows:** `gradlew.bat test -Dgrid`
* **Mac/Linux:** `./gradlew test -Dgrid`

Test Reports are stored in `build/reports/tests/test` for both execution methods.
If these folders don't exist, TestNG will create them during the test run.
