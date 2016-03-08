package com.fly.firefly.ui.object;

/**
 * Created by Dell on 3/2/2016.
 */
public class AboutUs {

    public AboutUs obj2;
    private String signature;
    private String nothing;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public AboutUs getObj2() {
        return obj2;
    }

    public void setObj2(AboutUs obj2) {
        this.obj2 = obj2;
    }

    public AboutUs() {
    }

    public AboutUs(AboutUs data) {

        nothing = data.getNothing();

    }

    public String getNothing() {
        return nothing;
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }
}


