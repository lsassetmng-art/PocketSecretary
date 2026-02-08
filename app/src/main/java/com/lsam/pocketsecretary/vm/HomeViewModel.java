package com.lsam.pocketsecretary.vm;

import com.lsam.pocketsecretary.model.HomeUiState;

public class HomeViewModel {

    private HomeUiState state = HomeUiState.INIT;

    public HomeUiState getState() {
        return state;
    }

    public void markIdle() {
        state = HomeUiState.IDLE;
    }
}
