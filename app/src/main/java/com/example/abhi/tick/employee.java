package com.example.abhi.tick;

public class employee {
    private String location;
    private String work;
    private String time;

    public employee(String location, String work, String time) {
        this.location = location;
        this.work = work;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getWork() {
        return work;
    }

    public String getTime() {
        return time;
    }
}
