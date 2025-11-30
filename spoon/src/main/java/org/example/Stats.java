package org.example;

public class Stats {
    private int reads = 0;
    private int writes = 0;
    private int searchedExpensive = 0;
    private int totalExpensiveProducts = 0;

    public int getReads(){return reads;}
    public int getWrites(){return writes;}
    public int getSearchedExpensive(){return searchedExpensive;}
    public int getTotalExpensiveProducts(){return totalExpensiveProducts;}

    public void incrrement(String action) {
        if (action.equals("read")) {reads++;}
        if (action.equals("write")) {writes++;}
    }

    public String userType(){
        if(reads>=writes) return "READ_ORIENTED";
        else return "WRITE_ORIENTED";
    }
    public void merge(Stats other) {
        this.reads += other.reads;
        this.writes += other.writes;
    }

    public void addExpensiveSearch(int count, int total) {
        this.searchedExpensive += count;
        this.totalExpensiveProducts = total;
    }

}
