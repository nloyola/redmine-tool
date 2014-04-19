package org.biobank;

import com.taskadapter.redmineapi.RedmineManager;

public class RedmineTool {

    private static String redmineHost = System.getProperty("redmine.host");

    private static String apiAccessKey = System.getProperty("redmine.access.key");

    private static String projectKey = System.getProperty("redmine.project.key");

    public static void main(String[] args) {
        if (redmineHost == null) {
            System.out.println("Error: redmine host not specified");
            System.exit(1);
        }

        if (apiAccessKey == null) {
            System.out.println("Error: redmine API key not specified");
            System.exit(1);
        }

        if (projectKey == null) {
            System.out.println("Error: redmine project key not specified");
            System.exit(1);
        }

        RedmineManager mgr = new RedmineManager(redmineHost, apiAccessKey);

        try {
            // IssueUpdater issueUpdater = IssueUpdater.getInstance();
            // issueUpdater.setRedmineManager(mgr);
            // issueUpdater.setProjectKey(projectKey);
            // issueUpdater.doWork();

            IssuesInfo issuesInfo = new IssuesInfo();
            issuesInfo.setRedmineManager(mgr);
            issuesInfo.setProjectKey(projectKey);
            issuesInfo.doWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
