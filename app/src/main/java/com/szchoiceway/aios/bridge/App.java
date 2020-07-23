package com.szchoiceway.aios.bridge;

import android.graphics.drawable.Drawable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return pkg.equals(app.pkg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkg);
    }
}
