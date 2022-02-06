package de.pinas.pedometerapp;

public class Globals {
    private static final Globals instance = new Globals();

    private float steps;
    private String date;
    private float light;

    private Globals() {
        this.steps = 0.0f;
        this.date = "";
        this.light = -1.0f;
    }

    public static Globals getInstance() {
        return instance;
    }

    public float getSteps() {
        return steps;
    }

    public void setSteps(float steps) {
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

}
