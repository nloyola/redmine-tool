package org.biobank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Version;

public class IssuesInfo implements IRedmineTask {

    private static Logger log = LoggerFactory.getLogger(IssuesInfo.class);

    private RedmineManager mgr;

    private String projectKey;

    @Override
    public void setRedmineManager(RedmineManager mgr) {
        this.mgr = mgr;

    }

    @Override
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;

    }

    @Override
    public void doWork() throws RedmineException {
        if (mgr == null) {
            throw new IllegalStateException("redmine manager has not been set");
        }

        if (projectKey == null) {
            throw new IllegalStateException("redmine proeject key has not been set");
        }

        // Project project = mgr.getProjectByKey(projectKey);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("project_id", projectKey);
        parameters.put("status_id", "open");
        List<Issue> issues = mgr.getIssues(parameters);

        for (Issue issue : issues) {
            String statusName = issue.getStatusName();
            Version targetVersion = issue.getTargetVersion();

            if ((!statusName.equals("New") && !statusName.equals("Assigned"))
                || (targetVersion == null)
                || !targetVersion.getName().equals("v3.9.0")) {
                continue;
            }

            StringBuffer buf = new StringBuffer();

            buf.append(issue.getId()).append("\n");
            buf.append(issue.getSubject()).append("\n");
            buf.append(issue.getDescription()).append("\n");
            buf.append("\n-------\n");

            log.info("issue: {}", buf.toString());
        }

    }

}
