package com.example.bjb2.Views;

public class VFModel {

    private static VFModel VFModel;
    private final ViewFactory viewFactory;

    private VFModel() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized VFModel getInstance() {
        if (VFModel == null) { VFModel = new VFModel(); }
        return VFModel;
    }

    public ViewFactory getVF() {
        return viewFactory;
    }
}
