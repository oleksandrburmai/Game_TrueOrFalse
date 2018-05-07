package com.alex.game;

public class Fact {

    private String fact;
    private boolean isTrue;

    Fact(String fact, boolean isTrue) {
        this.fact = fact;
        this.isTrue = isTrue;
    }

    public String getFact() {
        return fact;
    }

    public boolean getIsTrue() {
        return isTrue;
    }
}
