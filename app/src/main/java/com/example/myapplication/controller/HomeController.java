package com.example.myapplication.controller;

import com.example.myapplication.view.HomeActivity;

public class HomeController {
    private HomeActivity view;

    public HomeController(HomeActivity view) {
        this.view = view;
    }

    public void onGoClientButtonClicked() {
        view.navigateToClientActivity();
    }
}