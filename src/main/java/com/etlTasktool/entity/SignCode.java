package com.etlTasktool.entity;

/**
 * 标志代码
 */
public enum SignCode {
    Report("报表", "report"),
    QHDM("机构代码", "qhdm"),
    TK("台卡", "tk"),
    SUM("汇总", "sum"),
    TK_SUM("台卡汇总", "tksum"),
    All("全部","all");
    // 成员变量
    private String name;
    private String code;
    // 构造方法
    private SignCode(String name, String code) {
        this.name = name;
        this.code = code;
    }
    // 普通方法
    public static String getCode(String name) {
        for (SignCode c : SignCode.values()) {
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
