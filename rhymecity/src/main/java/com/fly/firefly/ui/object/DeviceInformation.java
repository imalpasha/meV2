package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/9/2015.
 */
public class DeviceInformation extends BaseObj {

    private String sdkVersion;
    private String version;
    private String deviceId;
    private String brand;
    private String model;
    private String dataVersion;
    private String password;

    /*Initiate Class*/
    public DeviceInformation() {
    }

    /*Initiate Class With Data*/
    public DeviceInformation(DeviceInformation info){
        sdkVersion = info.getSdkVersion();
        version = info.getVersion();
        deviceId = info.getDeviceId();
        brand = info.getBrand();
        model = info.getModel();
        dataVersion = info.getDataVersion();
        signature = info.getSignature();
        username = info.getUsername();
        password = info.getPassword();
    }

    /*Setter & Getter*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
