package org.selenium.utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.selenium.utilities.ListenersImplementation.test;

public class ZephyrIntegration {

    private static ReadProperty readProperty = ReadProperty.getInstance();
    public static void setUpTestCycle() throws Exception {
        String folderId ="9196748";
        List<String> supportedEnv = Arrays.asList("QA", "DEV", "PROD", "UAT");
        String environmentFromProps = readProperty.readProperties("environment");
        if (environmentFromProps != null) {
            if (!supportedEnv.contains(environmentFromProps.trim().toUpperCase()))
                throw new Exception("Invalid environment '" + environmentFromProps + "' set from Properties. No tests will execute");
            System.out.println("ENVIRONMENT SET FROM PROPERTIES:");
        } else
            throw new Exception("No environment has been set either from properties file or system variable/ CLI.");
        if (StringUtils.equalsIgnoreCase(Objects.requireNonNull(readProperty.readProperties("updateZephyr")), ("yes"))) {
            String url = readProperty.readProperties("zephyrCreateTestCycleURL");
            String testCyclePayload;
            String date = LocalDateTime.now()
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_DATE_TIME);
            String nextDate = LocalDateTime.now()
                    .plusDays(1)
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_DATE_TIME);
            try {
                final String payloadValue = System.getProperty("user.dir") +"\\sw_hcm_selenium\\payloads\\CreateTestCycle.json";
                testCyclePayload = Files.readString(Path.of(payloadValue));
                JsonHandler jsonHandler = new JsonHandler(testCyclePayload);
                testCyclePayload = jsonHandler.writeToJson("$.projectKey", readProperty.readProperties("zephyrProjectKey"));
                testCyclePayload = jsonHandler.writeToJson("$.name", "Automation Test Run_" + date);
                testCyclePayload = jsonHandler.writeToJson("$.description", "Automated Test Execution Run on " + date);
                testCyclePayload = jsonHandler.writeToJson("$.plannedStartDate", date);
                testCyclePayload = jsonHandler.writeToJson("$.plannedEndDate", nextDate);
                testCyclePayload = jsonHandler.writeToJson("$.ownerId", readProperty.readProperties("ownerId"));
                folderId = readProperty.readProperties("zephyrRegressionFolderId");
                testCyclePayload = jsonHandler.writeToJson("$.folderId", folderId);
                String responseBody = RestAssured
                        .given()
                        .relaxedHTTPSValidation()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + readProperty.readProperties("zephyrToken"))
                        .body(testCyclePayload)
                        .log().all()
                        .post(url).getBody().prettyPrint();
                JsonHandler responseHandler = new JsonHandler(responseBody);
                System.out.println("responseHandler = " + responseHandler);
                BaseTest.testCycKey = responseHandler.getJsonValue("$.key").toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface JiraData {
        //        String[] jiraId() default "";
        String jiraId() default "";
    }

    public static void zephyrTestResult(ITestResult result, Method m, long time) throws IOException, InterruptedException {
        String testCaseKey = null;
        long executiontime = time;
//        String[] testCaseKey =null;
        String currentMethodName = result.getMethod().getMethodName().trim();
        test.info("current Test Method Name :: " + currentMethodName);
        JiraData testData = m.getAnnotation(JiraData.class);
        if (m.isAnnotationPresent(JiraData.class)) {
            //Do something with testData.testId();
            testCaseKey = testData.jiraId();
        }
        if (StringUtils.equalsIgnoreCase( readProperty.readProperties("updateZephyr").trim(), "yes")) {
            String url = readProperty.readProperties("zephyrCreateExecutionURL");
            final String payloadValue = System.getProperty("user.dir") +"\\sw_hcm_selenium\\payloads\\CreateTestExecution.json";
            String payload = Files.readString(Path.of(payloadValue));
            String testStatus = String.valueOf(result.getStatus());
            String testName = result.getMethod().getMethodName();
            test.info("Test Status " + testStatus + "   " + "Test Name " + testName);
            if (testName != null) {
                JsonHandler reqJsonHandler = new JsonHandler(payload);
                payload = reqJsonHandler.writeToJson("$.projectKey", readProperty.readProperties("zephyrProjectKey"));
                payload = reqJsonHandler.writeToJson("$.testCycleKey", BaseTest.testCycKey);
                payload = reqJsonHandler.writeToJson("$.statusName", testStatus.equalsIgnoreCase("1") ? readProperty.readProperties("zephyrPassStatus") : readProperty.readProperties("zephyrFailStatus"));
                payload = reqJsonHandler.writeToJson("$.testCaseKey", testCaseKey);
                payload = reqJsonHandler.writeToJson("$.executionTime", executiontime);
                String responseBody = RestAssured
                        .given()
                        .relaxedHTTPSValidation()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + readProperty.readProperties("zephyrToken"))
                        .body(payload)
                        .log().all()
                        .post(url).getBody().prettyPrint();
            }
        }
    }
}
