package com.fly.firefly.ui.object;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dell on 2/11/2016.
 */
public class BoardingPassObj extends RealmObject{

    private String pnr;
    private String username;
    private String boardingPassObj;

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBoardingPassObj() {
        return boardingPassObj;
    }

    public void setBoardingPassObj(String boardingPassObj) {
        this.boardingPassObj = boardingPassObj;
    }

}
