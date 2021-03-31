package com.etlTasktool.tools;

import com.etlTasktool.App;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static java.lang.Thread.sleep;

/**
 * 自动化工具类
 * 主要用于自动获取cookie
 */
public class SeleniumTool {

    /**
     * 登陆地址
     */
    private static final String LOGIN_URL = "https://zwxt.mca.gov.cn/yhdsep/template/login.html";

    public  void setCookie() {
        try {
            System.setProperty("webdriver.chrome.driver", this.getClass().getClassLoader().getResource("chromedriver.exe").getPath());
            ChromeOptions options = new ChromeOptions();
            WebDriver driver = new ChromeDriver(options);
            driver.get(LOGIN_URL);
            driver.findElement(By.id("username")).sendKeys("developer");
            driver.findElement(By.id("password")).sendKeys("YHmz@1301");
            driver.findElement(By.id("sbbtn")).click();
            sleep(5000);

            WebDriver.Options manage = driver.manage();
            Set<Cookie> cookies = manage.getCookies();
            Map<String, String> cookiesMap = new HashMap<>();
            String cookie = "";
            for (Cookie c : cookies) {
                cookie = cookie + c.getName() + "=" + c.getValue() + ";";
                cookiesMap.put(c.getName(), c.getValue());
            }
            cookie = cookie.substring(0, cookie.length() - 1);
            String x_xsrf_token = cookiesMap.get("XSRF-TOKEN");
            driver.quit();
//        更新文件中的cookie
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getClass().getClassLoader().getResource("app.properties").getPath(), false));
            out.write("COOKIE=" + cookie);
            out.write("\nX_XSRF_TOKEN=" + x_xsrf_token);
            out.flush();
            out.close();
//        更新properties中的值
            App.setProperties(new PropertyReaderTool());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
