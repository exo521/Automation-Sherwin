package org.selenium.utilities;

import com.aventstack.extentreports.ExtentTest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static org.selenium.utilities.BaseTest.*;

public class ListenersImplementation extends ExtentReportManager implements ITestListener {

    public static ExtentTest test;

    public void onTestStart(ITestResult result) {
        test = report.createTest(result.getMethod().getMethodName());
// TODO Auto-generated method stub
    }


    public void onTestSuccess(ITestResult result) {
// TODO Auto-generated method stub
        System.out.println("Success of test cases and its details are : " + result.getName());
    }


    public void onTestFailure(ITestResult result) {
// TODO Auto-generated method stub
        System.out.println("Failure of test cases and its details are : " + result.getName());
        if(jiraFlag.equalsIgnoreCase("yes")){
            // raise jira ticket:
            System.out.println("is ticket ready for JIRA: " + jiraFlag);
            JiraServiceProvider jiraSp = new JiraServiceProvider(jiraURL,
                    jiraUserName, jiraAPIToken, zephyrProjectKey);
            String issueSummary = "Automation Script Failure for "+result.getMethod().getConstructorOrMethod().getMethod().getName();
            String issueDescription = result.getThrowable().getMessage() + "\n";
            issueDescription.concat(ExceptionUtils.getFullStackTrace(result.getThrowable()));
            //To split the String
            String[] arrOfStr = issueDescription.split("org.openqa.selenium");
            System.out.println("arrayZero = " + arrOfStr[0]);
//            jiraSp.createJiraTicket("Defect", issueSummary, issueDescription);
            jiraSp.createJiraTicket("Defect", issueSummary, arrOfStr[0]);
        }
    }


    public void onTestSkipped(ITestResult result) {
// TODO Auto-generated method stub
        System.out.println("Skip of test cases and its details are : " + result.getName());
    }


    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
// TODO Auto-generated method stub
        System.out.println("Failure of test cases and its details are : " + result.getName());
    }


    public void onStart(ITestContext context) {
// TODO Auto-generated method stub
        report = ExtentReportManager.getReport();
    }

    public void onFinish(ITestContext context) {
// TODO Auto-generated method stub
        report.flush();
    }


}
