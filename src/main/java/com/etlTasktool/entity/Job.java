package com.etlTasktool.entity;

public class Job {

    private String jobName;
//    归属地id
    private String taskTypeId;
//    任务名称
    private String taskName;
    private String executeServer;
//    任务id
    private String taskId;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getExecuteServer() {
        return executeServer;
    }

    public void setExecuteServer(String executeServer) {
        this.executeServer = executeServer;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
