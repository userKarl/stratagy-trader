/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcInputExecOrderField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcInputExecOrderField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcInputExecOrderField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcInputExecOrderField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_BrokerID_get(swigCPtr, this);
  }

  public void setInvestorID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InvestorID_set(swigCPtr, this, value);
  }

  public String getInvestorID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InvestorID_get(swigCPtr, this);
  }

  public void setInstrumentID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InstrumentID_set(swigCPtr, this, value);
  }

  public String getInstrumentID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InstrumentID_get(swigCPtr, this);
  }

  public void setExecOrderRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ExecOrderRef_set(swigCPtr, this, value);
  }

  public String getExecOrderRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ExecOrderRef_get(swigCPtr, this);
  }

  public void setUserID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_UserID_set(swigCPtr, this, value);
  }

  public String getUserID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_UserID_get(swigCPtr, this);
  }

  public void setVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_Volume_set(swigCPtr, this, value);
  }

  public int getVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_Volume_get(swigCPtr, this);
  }

  public void setRequestID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_RequestID_set(swigCPtr, this, value);
  }

  public int getRequestID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_RequestID_get(swigCPtr, this);
  }

  public void setBusinessUnit(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_BusinessUnit_set(swigCPtr, this, value);
  }

  public String getBusinessUnit() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_BusinessUnit_get(swigCPtr, this);
  }

  public void setOffsetFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_OffsetFlag_set(swigCPtr, this, value);
  }

  public char getOffsetFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_OffsetFlag_get(swigCPtr, this);
  }

  public void setHedgeFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_HedgeFlag_set(swigCPtr, this, value);
  }

  public char getHedgeFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_HedgeFlag_get(swigCPtr, this);
  }

  public void setActionType(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ActionType_set(swigCPtr, this, value);
  }

  public char getActionType() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ActionType_get(swigCPtr, this);
  }

  public void setPosiDirection(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_PosiDirection_set(swigCPtr, this, value);
  }

  public char getPosiDirection() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_PosiDirection_get(swigCPtr, this);
  }

  public void setReservePositionFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ReservePositionFlag_set(swigCPtr, this, value);
  }

  public char getReservePositionFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ReservePositionFlag_get(swigCPtr, this);
  }

  public void setCloseFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_CloseFlag_set(swigCPtr, this, value);
  }

  public char getCloseFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_CloseFlag_get(swigCPtr, this);
  }

  public void setExchangeID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ExchangeID_set(swigCPtr, this, value);
  }

  public String getExchangeID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ExchangeID_get(swigCPtr, this);
  }

  public void setInvestUnitID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InvestUnitID_set(swigCPtr, this, value);
  }

  public String getInvestUnitID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_InvestUnitID_get(swigCPtr, this);
  }

  public void setAccountID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_AccountID_set(swigCPtr, this, value);
  }

  public String getAccountID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_AccountID_get(swigCPtr, this);
  }

  public void setCurrencyID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_CurrencyID_set(swigCPtr, this, value);
  }

  public String getCurrencyID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_CurrencyID_get(swigCPtr, this);
  }

  public void setClientID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ClientID_set(swigCPtr, this, value);
  }

  public String getClientID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_ClientID_get(swigCPtr, this);
  }

  public void setIPAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_IPAddress_set(swigCPtr, this, value);
  }

  public String getIPAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_IPAddress_get(swigCPtr, this);
  }

  public void setMacAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_MacAddress_set(swigCPtr, this, value);
  }

  public String getMacAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputExecOrderField_MacAddress_get(swigCPtr, this);
  }

  public CThostFtdcInputExecOrderField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcInputExecOrderField(), true);
  }

}
