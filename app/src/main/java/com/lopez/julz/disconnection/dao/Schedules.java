package com.lopez.julz.disconnection.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schedules {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo (name = "DisconnectorName")
    private String DisconnectorName;

    @ColumnInfo (name = "DisconnectorId")
    private String DisconnectorId;

    @ColumnInfo (name = "Day")
    private String Day;

    @ColumnInfo (name = "ServicePeriodEnd")
    private String ServicePeriodEnd;

    @ColumnInfo (name = "Routes")
    private String Routes;

    @ColumnInfo (name = "SequenceFrom")
    private String SequenceFrom;

    @ColumnInfo (name = "SequenceTo")
    private String SequenceTo;

    @ColumnInfo (name = "Status")
    private String Status;

    @ColumnInfo (name = "DatetimeDownloaded")
    private String DatetimeDownloaded;

    @ColumnInfo (name = "PhoneModel")
    private String PhoneModel;

    @ColumnInfo (name = "UserId")
    private String UserId;

    public Schedules() {
    }

    public Schedules(@NonNull String id, String disconnectorName, String disconnectorId, String day, String servicePeriodEnd, String routes, String sequenceFrom, String sequenceTo, String status, String datetimeDownloaded, String phoneModel, String userId) {
        this.id = id;
        DisconnectorName = disconnectorName;
        DisconnectorId = disconnectorId;
        Day = day;
        ServicePeriodEnd = servicePeriodEnd;
        Routes = routes;
        SequenceFrom = sequenceFrom;
        SequenceTo = sequenceTo;
        Status = status;
        DatetimeDownloaded = datetimeDownloaded;
        PhoneModel = phoneModel;
        UserId = userId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDisconnectorName() {
        return DisconnectorName;
    }

    public void setDisconnectorName(String disconnectorName) {
        DisconnectorName = disconnectorName;
    }

    public String getDisconnectorId() {
        return DisconnectorId;
    }

    public void setDisconnectorId(String disconnectorId) {
        DisconnectorId = disconnectorId;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getServicePeriodEnd() {
        return ServicePeriodEnd;
    }

    public void setServicePeriodEnd(String servicePeriodEnd) {
        ServicePeriodEnd = servicePeriodEnd;
    }

    public String getRoutes() {
        return Routes;
    }

    public void setRoutes(String routes) {
        Routes = routes;
    }

    public String getSequenceFrom() {
        return SequenceFrom;
    }

    public void setSequenceFrom(String sequenceFrom) {
        SequenceFrom = sequenceFrom;
    }

    public String getSequenceTo() {
        return SequenceTo;
    }

    public void setSequenceTo(String sequenceTo) {
        SequenceTo = sequenceTo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDatetimeDownloaded() {
        return DatetimeDownloaded;
    }

    public void setDatetimeDownloaded(String datetimeDownloaded) {
        DatetimeDownloaded = datetimeDownloaded;
    }

    public String getPhoneModel() {
        return PhoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        PhoneModel = phoneModel;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
