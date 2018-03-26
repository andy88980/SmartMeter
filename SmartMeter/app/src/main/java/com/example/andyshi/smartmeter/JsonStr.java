package com.example.andyshi.smartmeter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AndyShi on 2018/3/25.
 */

public class JsonStr {
    @SerializedName("action")
    private String action;

    @SerializedName("machine")
    private String machine;

    public void setAction(String action) {
        this.action = action;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }
}
