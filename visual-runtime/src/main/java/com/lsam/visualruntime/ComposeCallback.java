package com.lsam.visualruntime;

public interface ComposeCallback {
    void onSuccess(ComposeResult result);
    void onError(Exception e);
}
