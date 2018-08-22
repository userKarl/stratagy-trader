/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcInputQuoteField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcInputQuoteField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcInputQuoteField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcInputQuoteField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BrokerID_get(swigCPtr, this);
  }

  public void setInvestorID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InvestorID_set(swigCPtr, this, value);
  }

  public String getInvestorID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InvestorID_get(swigCPtr, this);
  }

  public void setInstrumentID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InstrumentID_set(swigCPtr, this, value);
  }

  public String getInstrumentID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InstrumentID_get(swigCPtr, this);
  }

  public void setQuoteRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_QuoteRef_set(swigCPtr, this, value);
  }

  public String getQuoteRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_QuoteRef_get(swigCPtr, this);
  }

  public void setUserID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_UserID_set(swigCPtr, this, value);
  }

  public String getUserID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_UserID_get(swigCPtr, this);
  }

  public void setAskPrice(double value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskPrice_set(swigCPtr, this, value);
  }

  public double getAskPrice() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskPrice_get(swigCPtr, this);
  }

  public void setBidPrice(double value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidPrice_set(swigCPtr, this, value);
  }

  public double getBidPrice() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidPrice_get(swigCPtr, this);
  }

  public void setAskVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskVolume_set(swigCPtr, this, value);
  }

  public int getAskVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskVolume_get(swigCPtr, this);
  }

  public void setBidVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidVolume_set(swigCPtr, this, value);
  }

  public int getBidVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidVolume_get(swigCPtr, this);
  }

  public void setRequestID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_RequestID_set(swigCPtr, this, value);
  }

  public int getRequestID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_RequestID_get(swigCPtr, this);
  }

  public void setBusinessUnit(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BusinessUnit_set(swigCPtr, this, value);
  }

  public String getBusinessUnit() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BusinessUnit_get(swigCPtr, this);
  }

  public void setAskOffsetFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskOffsetFlag_set(swigCPtr, this, value);
  }

  public char getAskOffsetFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskOffsetFlag_get(swigCPtr, this);
  }

  public void setBidOffsetFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidOffsetFlag_set(swigCPtr, this, value);
  }

  public char getBidOffsetFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidOffsetFlag_get(swigCPtr, this);
  }

  public void setAskHedgeFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskHedgeFlag_set(swigCPtr, this, value);
  }

  public char getAskHedgeFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskHedgeFlag_get(swigCPtr, this);
  }

  public void setBidHedgeFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidHedgeFlag_set(swigCPtr, this, value);
  }

  public char getBidHedgeFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidHedgeFlag_get(swigCPtr, this);
  }

  public void setAskOrderRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskOrderRef_set(swigCPtr, this, value);
  }

  public String getAskOrderRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_AskOrderRef_get(swigCPtr, this);
  }

  public void setBidOrderRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidOrderRef_set(swigCPtr, this, value);
  }

  public String getBidOrderRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_BidOrderRef_get(swigCPtr, this);
  }

  public void setForQuoteSysID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ForQuoteSysID_set(swigCPtr, this, value);
  }

  public String getForQuoteSysID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ForQuoteSysID_get(swigCPtr, this);
  }

  public void setExchangeID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ExchangeID_set(swigCPtr, this, value);
  }

  public String getExchangeID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ExchangeID_get(swigCPtr, this);
  }

  public void setInvestUnitID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InvestUnitID_set(swigCPtr, this, value);
  }

  public String getInvestUnitID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_InvestUnitID_get(swigCPtr, this);
  }

  public void setClientID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ClientID_set(swigCPtr, this, value);
  }

  public String getClientID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_ClientID_get(swigCPtr, this);
  }

  public void setIPAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_IPAddress_set(swigCPtr, this, value);
  }

  public String getIPAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_IPAddress_get(swigCPtr, this);
  }

  public void setMacAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_MacAddress_set(swigCPtr, this, value);
  }

  public String getMacAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputQuoteField_MacAddress_get(swigCPtr, this);
  }

  public CThostFtdcInputQuoteField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcInputQuoteField(), true);
  }

}
