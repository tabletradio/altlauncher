package com.szchoiceway.aios.bridge;

import android.graphics.drawable.Drawable;

public class App {
    private Drawable image;
    private String name;
    private String pkg;

    public App(Drawable img, String pckage, String nm){
        this.image = img;
        this.name = nm;
        this.pkg = pckage;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable img) {
        this.image = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
