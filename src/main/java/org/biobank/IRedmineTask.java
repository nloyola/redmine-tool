package org.biobank;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;

public interface IRedmineTask {

    public void setRedmineManager(RedmineManager mgr);

    public void setProjectKey(String projectKey);

    public void doWork() throws RedmineException;
}
