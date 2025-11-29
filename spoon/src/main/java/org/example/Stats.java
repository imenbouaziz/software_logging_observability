package org.example;

public class Stats {
    private int reads = 0;
    private int writes = 0;

    public int getReads(){return reads;}
    public int getWrites(){return writes;}

    public void incrrement(String action) {
        if (action.equals("read")) {reads++;}
        if (action.equals("write")) {writes++;}
    }

    public String userType(){
        if(reads>=writes) return "READ_ORIENTED";
        else return "WRITE_ORIENTED";
    }



}
