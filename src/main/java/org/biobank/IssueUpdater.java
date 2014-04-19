package org.biobank;

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

public class IssueUpdater implements IRedmineTask {

    private static Logger log = LoggerFactory.getLogger(IssueUpdater.class);

    private static IssueUpdater instance = null;

    private RedmineManager mgr;

    private String projectKey;

    public static IssueUpdater getInstance() {
        if (instance == null) {
            instance = new IssueUpdater();
        }
        return instance;
    }

    @Override
    public void doWork() throws RedmineException {
        if (mgr == null) {
            throw new IllegalStateException("redmine manager has not been set");
        }

        if (projectKey == null) {
            throw new IllegalStateException("redmine proeject key has not been set");
        }

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

    @Override
    public void setRedmineManager(RedmineManager mgr) {
        this.mgr = mgr;
    }

    @Override
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
}
