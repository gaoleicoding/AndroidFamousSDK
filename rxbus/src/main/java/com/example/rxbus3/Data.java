package com.example.rxbus3;

import java.io.Serializable;


/**
 * base entity of all data entity json
 *
 * @{#} Data.java Create on 2013-6-29 下午7:12:34
 * <p>
 * class desc:
 *
 * <p>Copyright: Copyright(c) 2013 </p>
 * @Version 1.0
 * @Author <a href="mailto:kris@krislq.com">Kris.lee</a>
 */
public class Data implements Serializable {
    private String ver;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

}
