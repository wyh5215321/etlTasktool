package com.etlTasktool.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReaderTool {
    private Properties prop;

    public PropertyReaderTool()
    {
        this.prop=new Properties();

        try {
            FileInputStream in=new FileInputStream(new File(PropertyReaderTool.class.getClassLoader().getResource("app.properties").getPath()));
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
