package com.example.rxbus3;

import java.io.Serializable;

/**
 * @{#} ICarsClubResponse.java Create on 2013-6-29 下午7:01:23
 * <p>
 * class desc:
 *
 * <p>Copyright: Copyright(c) 2013 </p>
 * @Version 1.0
 * @Author <a href="mailto:kris@krislq.com">Kris.lee</a>
 */
public class ICarsClubResponse<D extends Data> implements Serializable {

    /**
     * serialVersionUID：
     */
    private static final long serialVersionUID = -5855835028187755682L;
    private Status status;
    private D data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }


    public static class Status {
        private int code;
        private String message;


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
