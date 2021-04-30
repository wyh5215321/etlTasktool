package com.etlTasktool;

import com.etlTasktool.entity.ProvinceCode;
import com.etlTasktool.entity.SignCode;
import com.etlTasktool.tools.JobTool;
import com.etlTasktool.tools.PropertyReaderTool;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class App
{

    /**
     * 配置文件读取工具
     */
    private static PropertyReaderTool properties= new PropertyReaderTool() ;



    public static void setProperties(PropertyReaderTool properties) {
        App.properties = properties;
    }

    /**
     * cookie必填
     */
    public static  String COOKIE =properties.getProperty("COOKIE") ;
    /**
     * x-xsrf-token必填
     */
    public static  String X_XSRF_TOKEN = properties.getProperty("X_XSRF_TOKEN");

    /**
     * 主函数入口
     * @param args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static void main( String[] args ) throws IOException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
//        执行抽取任务示例
//        String[] needExcuteList = {""};
//        String[] excludeExcuteList = {""};
//        excuteJobs(ProvinceCode.getCode("云南"),SignCode.getCode("全部"),null,null,null);
        getWaitingExcuteJobList(ProvinceCode.getCode("浙江"), SignCode.getCode("台卡"), null, null, null);

//       收集执行结果
//       JobTool.collectResult(ProvinceCode.getCode("北京"),"2021-04-19",SignCode.getCode("台卡汇总"),null, null,null);

//       执行结果并收集  (不常用)
//        excuteJobsAndCollectResult(ProvinceCode.getCode("云南"),SignCode.getCode("全部"),null,null,null);


    }

    /**
     * 执行抽取任务
     * @param taskTypeId
     * @param sign
     * @param needExcuteList
     * @param excludeExcuteList
     * @throws IOException
     */
    public static  void excuteJobs(String taskTypeId, String sign, String[] needExcuteList, String[] excludeExcuteList,String interruptJobName) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        JobTool jobTool = new JobTool(taskTypeId);
        jobTool.excuteJobs(sign, needExcuteList==null?null:Arrays.asList(needExcuteList), excludeExcuteList==null?null:Arrays.asList(excludeExcuteList),interruptJobName);
    }


    /**
     * 执行抽取任务并进行采集
     * @param taskTypeId
     * @param sign
     * @param needExcuteList
     * @param excludeExcuteList
     * @param interruptJobName
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws InterruptedException
     */
    public static  void excuteJobsAndCollectResult(String taskTypeId, String sign, String[] needExcuteList, String[] excludeExcuteList,String interruptJobName) throws IOException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
        JobTool jobTool = new JobTool(taskTypeId);
        jobTool.excuteJobs(sign, needExcuteList==null?null:Arrays.asList(needExcuteList),excludeExcuteList==null?null: Arrays.asList(excludeExcuteList),interruptJobName);
        jobTool.collectResult();
    }

    /**
     * 获取等待执行队列
     * @param taskTypeId
     * @param sign
     * @param needExcuteList
     * @param excludeExcuteList
     * @throws IOException
     */
    public static int getWaitingExcuteJobList(String taskTypeId, String sign, String[] needExcuteList, String[] excludeExcuteList, String interruptJobName) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        JobTool jobTool = new JobTool(taskTypeId);
        List list = jobTool.getWaitingExcuteJobList(sign, needExcuteList==null?null:Arrays.asList(needExcuteList), excludeExcuteList==null?null: Arrays.asList(excludeExcuteList),interruptJobName);
        System.out.println("查询出执行队列共计：" + list.size());
        return list.size();
    }




}
