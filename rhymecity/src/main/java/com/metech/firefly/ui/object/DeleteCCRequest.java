package com.metech.firefly.ui.object;

/**
 * Created by Dell on 6/16/2016.
 */
public class DeleteCCRequest {

    private String personID;
    private String signature;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public DeleteCCRequest(){

    }

    public DeleteCCRequest(Signature data){

        signature = data.getSignature();
        personID = data.getPersonID();

    }

}
