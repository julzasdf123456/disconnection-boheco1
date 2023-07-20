package com.lopez.julz.disconnection.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DisconnectionList {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo (name = "ScheduleId")
    private String ScheduleId;

    @ColumnInfo (name = "DisconnectorName")
    private String DisconnectorName;

    @ColumnInfo (name = "UserId")
    private String UserId;

    @ColumnInfo (name = "AccountNumber")
    private String AccountNumber;

    @ColumnInfo (name = "ServicePeriodEnd")
    private String ServicePeriodEnd;

    @ColumnInfo (name = "AccountCoordinates")
    private String AccountCoordinates;

    @ColumnInfo (name = "Latitude")
    private String Latitude;

    @ColumnInfo (name = "Longitude")
    private String Longitude;

    @ColumnInfo (name = "Status")
    private String Status;

    @ColumnInfo (name = "Notes")
    private String Notes;

    @ColumnInfo (name = "NetAmount")
    private String NetAmount;

    @ColumnInfo (name = "Surcharge")
    private String Surcharge;

    @ColumnInfo (name = "ServiceFee")
    private String ServiceFee;

    @ColumnInfo (name = "Others")
    private String Others;

    @ColumnInfo (name = "PaidAmount")
    private String PaidAmount;

    @ColumnInfo (name = "ORNumber")
    private String ORNumber;

    @ColumnInfo (name = "ORDate")
    private String ORDate;

    @ColumnInfo (name = "UploadStatus")
    private String UploadStatus;

    @ColumnInfo(name = "ConsumerName")
    private String ConsumerName;

    @ColumnInfo(name = "ConsumerAddress")
    private String ConsumerAddress;

    @ColumnInfo(name = "MeterNumber")
    private String MeterNumber;

    @ColumnInfo(name = "PoleNumber")
    private String PoleNumber;

    @ColumnInfo(name = "DisconnectionDate")
    private String DisconnectionDate;

    @ColumnInfo(name = "LastReading")
    private String LastReading;

    @ColumnInfo(name = "Selected")
    private boolean Selected;

    public DisconnectionList() {
    }

    public DisconnectionList(@NonNull String id, String scheduleId, String disconnectorName, String userId, String accountNumber, String servicePeriodEnd, String accountCoordinates, String latitude, String longitude, String status, String notes, String netAmount, String surcharge, String serviceFee, String others, String paidAmount, String ORNumber, String ORDate, String uploadStatus, String consumerName, String consumerAddress, String meterNumber, String poleNumber, String disconnectionDate, String lastReading, boolean selected) {
        this.id = id;
        ScheduleId = scheduleId;
        DisconnectorName = disconnectorName;
        UserId = userId;
        AccountNumber = accountNumber;
        ServicePeriodEnd = servicePeriodEnd;
        AccountCoordinates = accountCoordinates;
        Latitude = latitude;
        Longitude = longitude;
        Status = status;
        Notes = notes;
        NetAmount = netAmount;
        Surcharge = surcharge;
        ServiceFee = serviceFee;
        Others = others;
        PaidAmount = paidAmount;
        this.ORNumber = ORNumber;
        this.ORDate = ORDate;
        UploadStatus = uploadStatus;
        ConsumerName = consumerName;
        ConsumerAddress = consumerAddress;
        MeterNumber = meterNumber;
        PoleNumber = poleNumber;
        DisconnectionDate = disconnectionDate;
        LastReading = lastReading;
        Selected = selected;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(String scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getDisconnectorName() {
        return DisconnectorName;
    }

    public void setDisconnectorName(String disconnectorName) {
        DisconnectorName = disconnectorName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getServicePeriodEnd() {
        return ServicePeriodEnd;
    }

    public void setServicePeriodEnd(String servicePeriodEnd) {
        ServicePeriodEnd = servicePeriodEnd;
    }

    public String getAccountCoordinates() {
        return AccountCoordinates;
    }

    public void setAccountCoordinates(String accountCoordinates) {
        AccountCoordinates = accountCoordinates;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getSurcharge() {
        return Surcharge;
    }

    public void setSurcharge(String surcharge) {
        Surcharge = surcharge;
    }

    public String getServiceFee() {
        return ServiceFee;
    }

    public void setServiceFee(String serviceFee) {
        ServiceFee = serviceFee;
    }

    public String getOthers() {
        return Others;
    }

    public void setOthers(String others) {
        Others = others;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getORNumber() {
        return ORNumber;
    }

    public void setORNumber(String ORNumber) {
        this.ORNumber = ORNumber;
    }

    public String getORDate() {
        return ORDate;
    }

    public void setORDate(String ORDate) {
        this.ORDate = ORDate;
    }

    public String getUploadStatus() {
        return UploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        UploadStatus = uploadStatus;
    }

    public String getConsumerName() {
        return ConsumerName;
    }

    public void setConsumerName(String consumerName) {
        ConsumerName = consumerName;
    }

    public String getConsumerAddress() {
        return ConsumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        ConsumerAddress = consumerAddress;
    }

    public String getMeterNumber() {
        return MeterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        MeterNumber = meterNumber;
    }

    public String getPoleNumber() {
        return PoleNumber;
    }

    public void setPoleNumber(String poleNumber) {
        PoleNumber = poleNumber;
    }

    public String getDisconnectionDate() {
        return DisconnectionDate;
    }

    public void setDisconnectionDate(String disconnectionDate) {
        DisconnectionDate = disconnectionDate;
    }

    public String getLastReading() {
        return LastReading;
    }

    public void setLastReading(String lastReading) {
        LastReading = lastReading;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
