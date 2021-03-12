package com.etlTasktool;


import com.etlTasktool.tools.HttpTool;
import com.etlTasktool.tools.JobTool;
import com.etlTasktool.tools.PropertyReaderTool;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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
     * 省份任务代码
     */
    public static final String HUNAN = "1000000001274527";

    public static void main( String[] args ) throws IOException, InterruptedException {
//        执行抽取任务示例
        JobTool jobTool = new JobTool(HUNAN);
        String[] a = {"212"};
        jobTool.excuteJobs("tk", Arrays.asList(a), null);

////        收集执行结果
        jobTool.collectExcuteResult();




    }
}
