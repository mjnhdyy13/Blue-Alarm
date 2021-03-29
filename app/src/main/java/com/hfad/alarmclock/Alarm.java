package com.hfad.alarmclock;

public class Alarm {
    private String time;
    private String level;
    private Boolean checkswitch;
    private String reqcode;
    private int pos;

    public Alarm(String time, String level,String reqcode,Boolean checkswitch, int pos) {
        this.time = time;// thời gian báo thức
        this.level = level;// lặp lại hay ko
        this.checkswitch = checkswitch;// kiểm tra switch on of off
        this.reqcode = reqcode;// code of pendingintent
    }

    public String getTime() {
        return time;
    }

    public String getReqcode() {
        return reqcode;
    }

    public String getLevel() {
        return level;
    }

    public Boolean getCheckswitch() {
        return checkswitch;
    }
    public int getPos(){
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setCheckswitch(Boolean checkswitch) {
        this.checkswitch = checkswitch;
    }
}
