package com.etlTasktool.entity;

public class JobExcuteResult extends Job {
    /**
     * 抽取数据量
     */
    private int extractAmount;

    /**
     * 新增抽取数据量
     */
    private int loadInsertAmount;

    /**
     *新增异常数据量
     */
    private int errorInsertAmount;

    /**
     * 异常总量
     */
    private int allError;

    /**
     * 结果状态
     */
    private String executeStatus;

    /**
     * 实际结束时间
     */
    private String executeEndtTime;

    /**
     * 实际开始时间
     * @return
     */

    private String executeStartTime;

    public int getExtractAmount() {
        return extractAmount;
    }

    public void setExtractAmount(int extractAmount) {
        this.extractAmount = extractAmount;
    }

    public int getLoadInsertAmount() {
        return loadInsertAmount;
    }

    public void setLoadInsertAmount(int loadInsertAmount) {
        this.loadInsertAmount = loadInsertAmount;
    }

    public int getErrorInsertAmount() {
        return errorInsertAmount;
    }

    public void setErrorInsertAmount(int errorInsertAmount) {
        this.errorInsertAmount = errorInsertAmount;
    }

    public int getAllError() {
        return allError;
    }

    public void setAllError(int allError) {
        this.allError = allError;
    }

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getExecuteEndtTime() {
        return executeEndtTime;
    }

    public void setExecuteEndtTime(String executeEndtTime) {
        this.executeEndtTime = executeEndtTime;
    }

    public String getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(String executeStartTime) {
        this.executeStartTime = executeStartTime;
    }
}
