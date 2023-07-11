package com.lopez.julz.disconnection.objects;

public class DiscoGroup {
    private String Town, MeterReader, GroupCode, ServicePeriod;

    public DiscoGroup() {
    }

    public DiscoGroup(String town, String meterReader, String groupCode, String servicePeriod) {
        Town = town;
        MeterReader = meterReader;
        GroupCode = groupCode;
        ServicePeriod = servicePeriod;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getMeterReader() {
        return MeterReader;
    }

    public void setMeterReader(String meterReader) {
        MeterReader = meterReader;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getServicePeriod() {
        return ServicePeriod;
    }

    public void setServicePeriod(String servicePeriod) {
        ServicePeriod = servicePeriod;
    }
}
