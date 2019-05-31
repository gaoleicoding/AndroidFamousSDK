package com.example.rxbus3;

/**
 * RxEvent
 *
 * @author misha
 * @date 2017/5/18
 */

public class RxEvent {

    private int eventCode;

    private Object content;

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
