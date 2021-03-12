package com.etlTasktool.tools;

import com.alibaba.fastjson.JSONObject;
import com.etlTasktool.App;
import com.etlTasktool.entity.Job;
import com.etlTasktool.entity.JobExcuteResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.lang.Thread.sleep;

public class JobTool {

    private String taskTypeId;

    private String excuteStarttime;


    private List<String> excuteTaskIdList=new ArrayList<>();

    public JobTool(String taskTypeId){
        this.taskTypeId = taskTypeId;
    }

    public String getExcuteStarttime() {
        return excuteStarttime;
    }

    public void setExcuteStarttime(String excuteStarttime) {
        this.excuteStarttime = excuteStarttime;
    }

    public List<String> getExcuteTaskIdList() {
        return excuteTaskIdList;
    }

    public void setExcuteTaskIdList(List<String> excuteTaskIdList) {
        this.excuteTaskIdList = excuteTaskIdList;
    }

    /**
     * 查询抽取结果状态
     * @param taskTypeId
     * @param url
     * @return
     * @throws IOException
     */
    public static <T> List<T> queryJobList(String taskTypeId,String url,Class T) throws IOException {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        List<T> list = new ArrayList<>();

        headers.put("Cookie", App.COOKIE);
        headers.put("X-XSRF-TOKEN", App.X_XSRF_TOKEN);

        parms.put("pageNumber", "1");
        parms.put("pageSize", "100");
        parms.put("taskTypeId", taskTypeId);

        while (true) {
            JSONObject jsonObject = HttpTool.post(url, headers, parms);

            if (!"200".equals(jsonObject.get("code").toString())) {
                System.out.println(jsonObject.toString());
                System.exit(0);
            }
            if (!"{}".equals(jsonObject.get("data").toString())) {
                list=  jsonObject.getJSONObject("data").getJSONObject("pageBean").getJSONArray("list").toJavaList(T);
                break;
            }else {
                System.out.println(jsonObject.toString());
                if ("403".equals(jsonObject.getJSONArray("errors").getJSONObject(0).getString("errorCode"))) {
                    System.out.println("-----cookie失效------");
                    System.exit(0);
                }
                System.out.println("-----调用查询接口失败，尝试重新发起请求------");
            }

        }

        return list;
    }

    /**
     * 查询任务
     * @return
     * @throws IOException
     */
    public  List<Job> getAllJobList() throws IOException {
        if (this.taskTypeId == null || this.taskTypeId.isEmpty()) {
            System.out.println("JobTool对象没有设置taskTypeId！！");
            return new ArrayList<>();
        }
        return queryJobList(this.taskTypeId, HttpTool.QUERY_ALL_JOB_URL,Job.class);
    }

    /**
     * 查询完成队列
     * @return
     * @throws IOException
     */
    public  List<JobExcuteResult> getAllEndList() throws IOException {
        if (this.taskTypeId == null || this.taskTypeId.isEmpty()) {
            System.out.println("JobTool对象没有设置taskTypeId！！");
            return new ArrayList<>();
        }
        return queryJobList(this.taskTypeId, HttpTool.QUERY_ALL_END_URL, JobExcuteResult.class);
    }

    /**
     * 查询无变更队列
     * @return
     * @throws IOException
     */
    public  List<JobExcuteResult> getNoChangeList() throws IOException {
        if (this.taskTypeId == null || this.taskTypeId.isEmpty()) {
            System.out.println("JobTool对象没有设置taskTypeId！！");
            return new ArrayList<>();
        }
        return queryJobList(this.taskTypeId, HttpTool.QUERY_NO_CHANGE_URL,JobExcuteResult.class);
    }

    /**
     * 执行抽取
     * @param job 需要执行的任务
     */
    public  boolean excuteJob(Job job) throws IOException {
        if (job.getTaskId()==null||job.getTaskId().isEmpty()) {
            System.out.println("Job对象没有设置taskid!!");
            System.exit(0);
        }
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", App.COOKIE);
        headers.put("X-XSRF-TOKEN", App.X_XSRF_TOKEN);

        Map<String, String> parms = new HashMap<>();
        parms.put("jobName", job.getJobName());
        parms.put("serverName", job.getExecuteServer());
        parms.put("taskId", job.getTaskId());
        parms.put("taskType", "01");
        parms.put("kettleType","1");


        BufferedWriter out = new BufferedWriter(new FileWriter("result.txt",true));
        String time=TimeTool.getTime();
        System.out.println(time+" ------- 【"+job.getTaskName()+"】执行请求发起");
        out.write("\n"+time+" ------- 【"+job.getTaskName()+"】执行请求发起");
        JSONObject jsonObject = HttpTool.post(HttpTool.IMPLEMENT, headers, parms);

        time=TimeTool.getTime();
        if ("200".equals(jsonObject.getString("code"))) {
            if ("开始执行".equals(jsonObject.getJSONObject("data").getString("data"))) {
                System.out.println(time+" ------- 【"+job.getTaskName()+"】执行完成");
                out.write("\n"+time+" ------- 【"+job.getTaskName()+"】执行完成");
                out.close();
                excuteTaskIdList.add(job.getTaskId());
                return true;
            }else {
                System.out.println(time+" ------- 【"+job.getTaskName()+"】执行失败");
                out.write("\n"+time+" ------- 【"+job.getTaskName()+"】执行失败");
                out.close();
                return false;
            }
        }else {
            System.out.println(time+" ------- 【"+job.getTaskName()+"】执行失败");
            out.write("\n"+time+" ------- 【"+job.getTaskName()+"】执行失败");
            out.close();
            return false;
        }
    }

    /**
     * todo 支持排除抽取  只执行台卡
     * @param sign   第一次过滤标志
     * <p>
     *     all代表过滤全部
     *     tk代表过滤台卡
     *     sum代表汇总
     *     tksum代表台卡和汇总
     *     report代表报表和数据表
     *     qhdm代表
     * </p>
     * @param needExcuteList  二次过滤条件  在taskname中进行匹配
     * @param excludeExcuteList 三次排除条件  排除数据
     * @throws IOException
     */
    public void excuteJobs(String sign,List<String> needExcuteList,List<String> excludeExcuteList) throws IOException {
//            查询所有的执行任务
        List<Job> allJobList = getAllJobList();
        List<Job> waitingJoblist = new ArrayList<>();
        if ("all".equals(sign.toLowerCase())) {
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList);
        } else if ("tk".equals(sign.toLowerCase())) {

            Predicate<Job> predicate1 = job -> {
                return job.getTaskName().contains("T");
            };
            Predicate<Job> predicate2 = job -> {
                return !(job.getTaskName().contains("sum")||job.getTaskName().contains("汇总"));
            };
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList.stream().filter(predicate1.and(predicate2)).collect(Collectors.toList()));
        } else if ("sum".equals(sign.toLowerCase())) {
            Predicate<Job> predicate1 = job -> {
                return job.getTaskName().contains("T");
            };
            Predicate<Job> predicate2 = job -> {
                return (job.getTaskName().contains("sum")||job.getTaskName().contains("汇总"));
            };
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList.stream().filter(predicate1.and(predicate2)).collect(Collectors.toList()));
        } else if ("tksum".equals(sign.toLowerCase())) {
            Predicate<Job> predicate1 = job -> {
                return job.getTaskName().contains("T");
            };
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList.stream().filter(predicate1).collect(Collectors.toList()));
        } else if ("report".equals(sign.toLowerCase())) {
            Predicate<Job> predicate1 = job -> {
                return job.getTaskName().contains("报表");
            };
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList.stream().filter(predicate1).collect(Collectors.toList()));
        } else if ("qhdm".equals(sign.toLowerCase())){
            Predicate<Job> predicate1 = job -> {
                return job.getTaskName().contains("代码");
            };
            waitingJoblist.clear();
            waitingJoblist.addAll(allJobList.stream().filter(predicate1).collect(Collectors.toList()));
        }
//        waitingJoblist经过了第一次过滤
        if (needExcuteList != null && !needExcuteList.isEmpty()) {
            List<Job> tempJoblist = new ArrayList<>();
            needExcuteList.stream().forEach(con -> {
                Predicate<Job> predicate = job -> {
                    return job.getTaskName().contains(con);
                };
                tempJoblist.addAll(waitingJoblist.stream().filter(predicate).collect(Collectors.toList()));
            });
            waitingJoblist.clear();
            waitingJoblist.addAll(tempJoblist);

        }

        if (excludeExcuteList != null && !excludeExcuteList.isEmpty()) {
           List<Job> tempJoblist = new ArrayList<>();
            excludeExcuteList.stream().forEach(con -> {
                Predicate<Job> predicate = job -> {
                    return job.getTaskName().contains(con);
                };
                tempJoblist.addAll(waitingJoblist.stream().filter(predicate.negate()).collect(Collectors.toList()));
                waitingJoblist.clear();
                waitingJoblist.addAll(tempJoblist);
                tempJoblist.clear();
            });
        }

        this.excuteStarttime = TimeTool.getDateString();
        int num = 0;
        BufferedWriter out = new BufferedWriter(new FileWriter("result.txt",true));

        while (!waitingJoblist.isEmpty()) {

            waitingJoblist.sort(Comparator.comparing(Job::getTaskName));
            num++;
            int size = waitingJoblist.size();
            AtomicInteger size1 = new AtomicInteger(size);
            out.write("\n-----第"+num+"次执行开始-----");
            out.close();
            System.out.println("-----第"+num+"次执行开始-----");
            List<Job> joblist=waitingJoblist.stream().filter(job->{
                try {
                    boolean bool = excuteJob(job);
                    System.out.println("-----还剩下"+ (size1.decrementAndGet())+"条任务待执行-----");
                    if (bool == true) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }).collect(Collectors.toList());
            waitingJoblist.clear();
            waitingJoblist.addAll(joblist);

            out = new BufferedWriter(new FileWriter("result.txt",true));
            System.out.println("-----第"+num+"次执行完成，共执行"+size+"条任务，成功"+(size-waitingJoblist.size())+"条，失败"+waitingJoblist.size()+"条-----");
            out.write("\n-----第"+num+"次执行完成，共执行"+size+"条任务，成功"+(size-waitingJoblist.size())+"条，失败"+waitingJoblist.size()+"条-----");
        }
        out.close();
    }


    /**
     * todo 修改为多线程
     * @throws IOException
     */
    public List<JobExcuteResult> collectExcuteResult() throws IOException, InterruptedException {
        List<JobExcuteResult> results= new ArrayList<>();
        int i = 0;
//        开始轮询执行结果
        List<JobExcuteResult> endResult = new ArrayList<>();
        BufferedWriter out = new BufferedWriter(new FileWriter("ExcuteResult.txt",true));
        while (!this.excuteTaskIdList.isEmpty()) {
//            先遍历执行完成队列
            results = getAllEndList();
            results=  results.stream().filter(result->{
                return result.getExecuteStartTime().equals(this.excuteStarttime)&&result.getExecuteEndtTime().equals(this.excuteStarttime);
            }).collect(Collectors.toList());

            results.stream().forEach(jobExcuteResult ->{
                if (this.excuteTaskIdList.contains(jobExcuteResult.getTaskId())) {
//                    执行完成，读取抽取数量
                    if (jobExcuteResult.getExecuteStatus().equals("06")) {
                        System.out.println("【"+jobExcuteResult.getTaskName()+"】执行完成；"+"总抽取量："
                                +jobExcuteResult.getExtractAmount()
                                +",新增插入量："+
                                jobExcuteResult.getLoadInsertAmount()+
                                ",新增异常插入量："
                                +jobExcuteResult.getErrorInsertAmount()
                                +",异常总量："+jobExcuteResult.getAllError());
                        try {
                            out.write(jobExcuteResult.getTaskName()+"执行完成；"+"总抽取量："
                                    +jobExcuteResult.getExtractAmount()
                                    +",新增插入量："+
                                    jobExcuteResult.getLoadInsertAmount()+
                                    ",新增异常插入量："
                                    +jobExcuteResult.getErrorInsertAmount()
                                    +",异常总量："+jobExcuteResult.getAllError());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        endResult.add(jobExcuteResult);
                        this.excuteTaskIdList.remove(jobExcuteResult.getTaskId());
                    }else if (jobExcuteResult.getExecuteStatus().equals("05")){
                        System.out.println("【"+jobExcuteResult.getTaskName()+"】"+"执行异常");
                        try {
                            out.write(jobExcuteResult.getTaskName()+" 执行异常");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.excuteTaskIdList.remove(jobExcuteResult.getTaskId());
                    }
                }
            });

            results = getNoChangeList();
            results=  results.stream().filter(result->{
                return result.getExecuteStartTime().equals(this.excuteStarttime)&&result.getExecuteEndtTime().equals(this.excuteStarttime);
            }).collect(Collectors.toList());

            results.stream().forEach(jobExcuteResult ->{
                if (this.excuteTaskIdList.contains(jobExcuteResult.getTaskId())) {
//                    执行完成，读取抽取数量
                    if (jobExcuteResult.getExecuteStatus().equals("06")) {
                        System.out.println("【"+jobExcuteResult.getTaskName()+"】执行完成；"+"总抽取量："
                                +jobExcuteResult.getExtractAmount()
                                +",新增插入量："+
                                jobExcuteResult.getLoadInsertAmount()+
                                ",新增异常插入量："
                                +jobExcuteResult.getErrorInsertAmount()
                                +",异常总量："+jobExcuteResult.getAllError());
                        try {
                            out.write(jobExcuteResult.getTaskName()+"执行完成；"+"总抽取量："
                                    +jobExcuteResult.getExtractAmount()
                                    +",新增插入量："+
                                    jobExcuteResult.getLoadInsertAmount()+
                                    ",新增异常插入量："
                                    +jobExcuteResult.getErrorInsertAmount()
                                    +",异常总量："+jobExcuteResult.getAllError());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        endResult.add(jobExcuteResult);
                        this.excuteTaskIdList.remove(jobExcuteResult.getTaskId());
                    }else if (jobExcuteResult.getExecuteStatus().equals("05")){
                        System.out.println("【"+jobExcuteResult.getTaskName()+"】"+"执行异常");
                        try {
                            out.write(jobExcuteResult.getTaskName()+" 执行异常");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.excuteTaskIdList.remove(jobExcuteResult.getTaskId());
                    }
                }
            });
//            休眠3秒再次请求
            sleep(3000);
        }
        out.close();
        return endResult;
    }

    /**
     * 执行抽取任务并统计最终抽取结果
     */
    public static void excuteJobsAndCollect(String taskTypeId, String isAll, List<String> needExcuteList,List<String> excludeExcuteList) throws IOException, InterruptedException {
        JobTool jobTool = new JobTool(taskTypeId);
        jobTool.excuteJobs(isAll,needExcuteList,excludeExcuteList);
        jobTool.collectExcuteResult();
    }

    /**
     * 执行抽取任务
     * @param taskTypeId
     * @param isAll
     * @param needExcuteList
     * @throws IOException
     */
    public static void excuteJobs(String taskTypeId, String isAll, List<String> needExcuteList, List<String> excludeExcuteList) throws IOException {
        JobTool jobTool = new JobTool(taskTypeId);
        jobTool.excuteJobs(isAll,needExcuteList,excludeExcuteList);
    }

}
