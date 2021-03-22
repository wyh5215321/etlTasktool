package com.etlTasktool.tools;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etlTasktool.App;
import com.etlTasktool.entity.HttpsTrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class HttpTool {

    /**
     * 查询无变更队列url
     */
    public static final String QUERY_NO_CHANGE_URL = "https://zwxt.mca.gov.cn/yhdsep/etlTaskExecuteQueue/queryNoChange.do";

    /**
     * 查询执行完成队列url
     */
    public static final String QUERY_ALL_END_URL = "https://zwxt.mca.gov.cn/yhdsep/etlTaskExecuteQueue/queryAllEnd.do";


    public static final String QUERY_ALL_JOB_URL = "https://zwxt.mca.gov.cn:/yhdsep/etlTaskRestService/queryAll.do";

    public static final String IMPLEMENT = "https://zwxt.mca.gov.cn/yhdsep/etlTaskExecuteQueue/implement.do";


    /**
     * post请求
     *
     * @throws IOException
     */
    public static JSONObject post(String url, Map<String, String> headers, Map<String, String> parms) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        SSLContext sslcontext = SSLContexts.custom().useSSL().build();
        sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(
                60000*3
        ).setConnectTimeout(
                60000*3
        ).build();
        httpPost.setConfig(requestConfig);
        if (!headers.isEmpty()) {
            Set<Map.Entry<String, String>> entrys = headers.entrySet();
            entrys.stream().forEach(entry -> {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            });
        }
        List<BasicNameValuePair> parameters = new ArrayList<>(2);
        if (!parms.isEmpty()) {
            Set<Map.Entry<String, String>> entrys = parms.entrySet();
            entrys.stream().forEach(entry -> {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            });
        }

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
        httpPost.setEntity(formEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity, "utf-8");
        JSONObject jsonObject = JSON.parseObject(string);
        response.close();
        httpClient.close();
        return jsonObject;
    }

    public static JSONObject get(String url) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X_XSRF_TOKEN", App.X_XSRF_TOKEN);
        httpGet.addHeader("COOKIE", App.COOKIE);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            JSONObject jsonObject = new JSONObject();
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String string = EntityUtils.toString(response.getEntity(), "utf-8");
                 jsonObject = JSON.parseObject(string);
                response.close();
            }
            return jsonObject;
        } finally {
            if (response != null) {
                response.close();
            }
            //相当于关闭浏览器
            httpclient.close();
        }

    }
}