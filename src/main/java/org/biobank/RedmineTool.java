package org.biobank;

import com.taskadapter.redmineapi.RedmineManager;

public class RedmineTool {

    private static String redmineHost = System.getProperty("redmine.host");

    private static String apiAccessKey = System.getProperty("redmine.access.key");

    private static String projectKey = System.getProperty("redmine.project.key");

    public static void main(String[] args) {
        RedmineManager mgr = new RedmineManager(redmineHost, apiAccessKey);
        try {
            IssueUpdater issueUpdater = IssueUpdater.getInstance();
            issueUpdater.setRedmineManager(mgr);
            issueUpdater.setProjectKey(projectKey);
            issueUpdater.doWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
