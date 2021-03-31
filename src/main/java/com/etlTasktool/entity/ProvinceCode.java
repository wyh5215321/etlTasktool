package com.etlTasktool.entity;

/**
 * 省份代码
 */
public enum ProvinceCode {
    HU_NAN("湖南", "1000000001285705"),
    YUN_NAN("云南", "1000000001057813"),
    BEI_JING("北京", "1000000001059917"),
    ZHE_JIANG("浙江", "1000000001072945"),
    SHAN_DONG("山东", "1000000000143130");
    // 成员变量
    private String name;
    private String code;
    // 构造方法
    private ProvinceCode(String name, String code) {
        this.name = name;
        this.code = code;
    }
    // 普通方法
    public static String getCode(String name) {
        for (ProvinceCode c : ProvinceCode.values()) {
            if (name.equals(c.getName())) {
                return c.getCode();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
