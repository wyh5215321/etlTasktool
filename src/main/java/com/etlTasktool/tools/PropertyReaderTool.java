package com.etlTasktool.tools;

import java.io.*;
import java.util.Properties;

/**
 * 文件读取工具类
 * <p>
 *     主要用于读取cookie信息
 * </p>
 */
public class PropertyReaderTool {
    private Properties prop;

    public PropertyReaderTool( )
    {
        this.prop=new Properties();
        try {
            File file = new File(this.getClass().getClassLoader().getResource("app.properties").getPath());
            FileInputStream in=new FileInputStream(file);
            this.prop.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key)
    {
        return prop.getProperty(key);
    }

    public void addProperty(String key,String value)
    {
        prop.put(key, value);
    }
}
