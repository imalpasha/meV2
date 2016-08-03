package com.metech.firefly.ui.object;

/**
 * Created by Dell on 6/14/2016.
 */
public class FriendFamilyDelete {

    private String deleteID;
    private String user_email;


    public FriendFamilyDelete(){
    }

    /*Initiate Class*/
    public FriendFamilyDelete(FriendFamilyDelete obj){
        deleteID = obj.getDeleteID();
        user_email = obj.getUser_email();
    }

    public String getDeleteID() {
        return deleteID;
    }

    public void setDeleteID(String deleteID) {
        this.deleteID = deleteID;
    }


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }


}
