package com.lopez.julz.disconnection.dao;

public class DiscoListGrouped {
    private String AccountNumber;
    private String ConsumerName;
    private String ConsumerAddress;
    private String MeterNumber;

    public DiscoListGrouped() {
    }

    public DiscoListGrouped(String accountNumber, String consumerName, String consumerAddress, String meterNumber) {
        AccountNumber = accountNumber;
        ConsumerName = consumerName;
        ConsumerAddress = consumerAddress;
        MeterNumber = meterNumber;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
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
}
