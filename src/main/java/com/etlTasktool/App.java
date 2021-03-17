package com.etlTasktool;


import com.etlTasktool.tools.JobTool;
import com.etlTasktool.tools.PropertyReaderTool;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


public class App
{

    private static final  PropertyReaderTool properties= new PropertyReaderTool() ;

    /**
     * cookie必填
     */
    public static final String COOKIE =properties.getProperty("COOKIE") ;
    /**
     * x-xsrf-token必填
     */
    public static final String X_XSRF_TOKEN = properties.getProperty("X_XSRF_TOKEN");


    /**
     * 湖南省份任务代码
     */
    public static final String HU_NAN = "1000000001285705";
    /**
     * 云南省份任务代码
     */
    public static final String YUN_NAN = "1000000001057813";
    /**
     * 北京省份任务代码
     */
    public static final String BEI_JING = "1000000001059917";
    /**
     * 浙江省份任务代码
     */
    public static final String ZHE_JIANG = "1000000001072945";
    /**
     * 山东省份任务代码
     */
    public static final String SHAN_DONG = "1000000000143130";


    public static void main( String[] args ) throws IOException, InterruptedException {
////        执行抽取任务示例
        JobTool jobTool = new JobTool(BEI_JING);
        String[] a = {"106","111","101_1"};
        jobTool.excuteJobs("tk",Arrays.asList(a), null,null);
//       收集执行结果
//        jobTool.collectResult();


//        JobTool.collectResult(BEI_JING,"2021-03-12","tk", Arrays.asList(a), null,null);


    }

    /**
     * 执行抽取任务
     * @param taskTypeId
     * @param sign
     * @param needExcuteList
     * @param excludeExcuteList
     * @throws IOException
     */
    public void excuteJobs(String taskTypeId, String sign, String[] needExcuteList, String[] excludeExcuteList,String interruptJobName) throws IOException {
        JobTool jobTool = new JobTool(taskTypeId);
        jobTool.excuteJobs(sign, Arrays.asList(needExcuteList), Arrays.asList(excludeExcuteList),interruptJobName);
    }

    /**
     * 获取等待执行队列
     * @param taskTypeId
     * @param sign
     * @param needExcuteList
     * @param excludeExcuteList
     * @throws IOException
     */
    public int getWaitingExcuteJobList(String taskTypeId, String sign, String[] needExcuteList, String[] excludeExcuteList,String interruptJobName) throws IOException {
        JobTool jobTool = new JobTool(taskTypeId);
        List list = jobTool.getWaitingExcuteJobList(sign, Arrays.asList(needExcuteList), Arrays.asList(excludeExcuteList),interruptJobName);
        System.out.println("查询出执行队列共计：" + list.size());
        return list.size();
    }



}
