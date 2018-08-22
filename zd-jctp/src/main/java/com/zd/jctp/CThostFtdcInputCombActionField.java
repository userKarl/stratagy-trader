/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcInputCombActionField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcInputCombActionField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcInputCombActionField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcInputCombActionField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_BrokerID_get(swigCPtr, this);
  }

  public void setInvestorID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InvestorID_set(swigCPtr, this, value);
  }

  public String getInvestorID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InvestorID_get(swigCPtr, this);
  }

  public void setInstrumentID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InstrumentID_set(swigCPtr, this, value);
  }

  public String getInstrumentID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InstrumentID_get(swigCPtr, this);
  }

  public void setCombActionRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_CombActionRef_set(swigCPtr, this, value);
  }

  public String getCombActionRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_CombActionRef_get(swigCPtr, this);
  }

  public void setUserID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_UserID_set(swigCPtr, this, value);
  }

  public String getUserID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_UserID_get(swigCPtr, this);
  }

  public void setDirection(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_Direction_set(swigCPtr, this, value);
  }

  public char getDirection() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_Direction_get(swigCPtr, this);
  }

  public void setVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_Volume_set(swigCPtr, this, value);
  }

  public int getVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_Volume_get(swigCPtr, this);
  }

  public void setCombDirection(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_CombDirection_set(swigCPtr, this, value);
  }

  public char getCombDirection() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_CombDirection_get(swigCPtr, this);
  }

  public void setHedgeFlag(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_HedgeFlag_set(swigCPtr, this, value);
  }

  public char getHedgeFlag() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_HedgeFlag_get(swigCPtr, this);
  }

  public void setExchangeID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_ExchangeID_set(swigCPtr, this, value);
  }

  public String getExchangeID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_ExchangeID_get(swigCPtr, this);
  }

  public void setIPAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_IPAddress_set(swigCPtr, this, value);
  }

  public String getIPAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_IPAddress_get(swigCPtr, this);
  }

  public void setMacAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_MacAddress_set(swigCPtr, this, value);
  }

  public String getMacAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_MacAddress_get(swigCPtr, this);
  }

  public void setInvestUnitID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InvestUnitID_set(swigCPtr, this, value);
  }

  public String getInvestUnitID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInputCombActionField_InvestUnitID_get(swigCPtr, this);
  }

  public CThostFtdcInputCombActionField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcInputCombActionField(), true);
  }

}
