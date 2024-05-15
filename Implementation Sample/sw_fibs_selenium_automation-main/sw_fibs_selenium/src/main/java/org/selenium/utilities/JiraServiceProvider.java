package org.selenium.utilities;

import net.rcarz.jiraclient.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.selenium.utilities.BaseTest.*;

public class JiraServiceProvider {
    public JiraClient jira;
    public String project;
    public ArrayList<String> versions = new ArrayList<String>();
    private static ReadProperty readProperty = ReadProperty.getInstance();
    public static final String affectsVersions = readProperty.readProperties("affectsVersion");
    public static final String priority = readProperty.readProperties("priority");
    public static final String assignee = readProperty.readProperties("assignee");
    public static final String duedate = readProperty.readProperties("duedate");
    public static final String labels = readProperty.readProperties("labels");
    public static String path;
    public ArrayList<String> label = new ArrayList<String>();
    public static String screenshotFolder = System.getProperty("user.dir") + "\\sw_emea_selenium\\screenshots\\";


    public JiraServiceProvider(String jiraUrl, String username, String password, String project) {
        BasicCredentials creds = new BasicCredentials(username, password);
        jira = new JiraClient(jiraUrl, creds);
        this.project = project;
    }

    public ArrayList<String> affectsVersion() {
        versions.add(affectsVersions);
        return versions;
    }

    public ArrayList<String> labels() {
        label.add(labels);
        return label;
    }

    public Date dueDateConversion() throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(duedate);
        return date;
    }

    public void fileUpload() {
        // Folder path containing the files
        String folderPath = screenshotFolder;
        // Get all files from the folder
        File folder = new File(folderPath);
        File[] fileList = folder.listFiles();
        File[] screenshotFiles = Arrays.stream(fileList)
                .filter(file -> file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))
                .toArray(File[]::new);
        long latestTimestamp = 0;
        File latestScreenshot = null;
        for (File screenshotFile : screenshotFiles) {
            long timestamp = screenshotFile.lastModified();
            if (timestamp > latestTimestamp) {
                latestTimestamp = timestamp;
                latestScreenshot = screenshotFile;
            }
        }
        if (latestScreenshot != null) {
            System.out.println("Latest screenshot: " + latestScreenshot.getName());
            path = latestScreenshot.getAbsolutePath();
        } else {
            System.out.println("No screenshots found in the folder.");
        }
    }

    public void createJiraTicket(String issueType, String summary, String description) {
        try {
            Issue.FluentCreate fleuntCreate = jira.createIssue(project, issueType);
            fleuntCreate.field(Field.VERSIONS, affectsVersion());
            fleuntCreate.field(Field.SUMMARY, summary);
            fleuntCreate.field(Field.DESCRIPTION,description);
            fleuntCreate.field(Field.PRIORITY, priority);
            fleuntCreate.field(Field.ASSIGNEE, assignee);
            fleuntCreate.field(Field.DUE_DATE, dueDateConversion());
            fleuntCreate.field(Field.LABELS, labels());
            fleuntCreate.field(Field.FIX_VERSIONS, affectsVersion());
            Issue newIssue = fleuntCreate.execute();
            fileUpload();
            attachFileToJiraIssue(String.valueOf(newIssue), path);
            System.out.println("new issue created in jira with ID: " + newIssue);
        } catch (JiraException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean attachFileToJiraIssue(String issueId, String screenShotPath) throws IOException, IOException {
        String jira_attachment_authentication = new String(
                org.apache.commons.codec.binary.Base64
                        .encodeBase64((jiraUserName + ":" + jiraAPIToken)
                                .getBytes()));
        CloseableHttpClient httpclient = HttpClients.createSystem();
        HttpPost httppost = new HttpPost(jiraURL + "/rest/api/3/issue/"
                + issueId + "/attachments");
        httppost.setHeader("X-Atlassian-Token", "no-check");
        httppost.setHeader("Authorization", "Basic "
                + jira_attachment_authentication);
        File fileToUpload = new File(screenShotPath);
        FileBody fileBody = new FileBody(fileToUpload);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", fileBody).build();
        httppost.setEntity(entity);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httppost);
        } finally {
            httpclient.close();
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("ScreenShot: " + screenShotPath
                    + " attached to the issue " + issueId);
            return true;
        } else {
            System.out.println("ScreenShot: " + screenShotPath
                    + " not attached to the issue " + issueId + " response is=" + response.getStatusLine().getStatusCode());
            return false;
        }

    }
}
