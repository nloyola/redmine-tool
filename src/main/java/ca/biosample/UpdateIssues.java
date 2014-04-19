package ca.biosample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Version;

public class UpdateIssues {

    private static Logger log = LoggerFactory.getLogger(UpdateIssues.class.getName());

    private static String redmineHost = System.getProperty("redmineHost");

    private static String apiAccessKey = System.getProperty("redmineAccessKey");

    private static String projectKey = System.getProperty("redmineProjectKey");

    public static void main(String[] args) {
        RedmineManager mgr = new RedmineManager(redmineHost, apiAccessKey);
        try {
            updateIssues(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateIssues(RedmineManager mgr)
        throws RedmineException {
        Project project = mgr.getProjectByKey(projectKey);

        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("project_id", projectKey);
        parameters.put("status_id", "open");

        List<Issue> issues = mgr.getIssues(parameters);
        Set<Issue> resolvedIssues = new HashSet<Issue>();

        for (Issue issue : issues) {
            if (!issue.getStatusId().equals(3)
                || !issue.getTargetVersion().getName().equals("v3.5.0"))
                continue;

            resolvedIssues.add(issue);

            log.trace(
                "issue: {}, target version: {}, status id: {}",
                new Object[] {
                    issue.toString(), issue.getTargetVersion().getName(),
                    issue.getStatusId()
                });
        }

        Version version333 = null;
        for (Version version : mgr.getVersions(project.getId())) {
            log.trace("version retrieved: {}", version.getName());
            if (version.getName().equals("v3.3.3")) {
                version333 = version;
            }
        }

        if (version333 == null) {
            throw new IllegalStateException("could not find version 3.3.3");
        }

        for (Issue issue : resolvedIssues) {
            issue.setTargetVersion(version333);
            mgr.update(issue);
        }

        log.trace("number of resolved issues: {}", resolvedIssues.size());
    }
}
