/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcRspUserLogin2Field {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcRspUserLogin2Field(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcRspUserLogin2Field obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcRspUserLogin2Field(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setTradingDay(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_TradingDay_set(swigCPtr, this, value);
  }

  public String getTradingDay() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_TradingDay_get(swigCPtr, this);
  }

  public void setLoginTime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_LoginTime_set(swigCPtr, this, value);
  }

  public String getLoginTime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_LoginTime_get(swigCPtr, this);
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_BrokerID_get(swigCPtr, this);
  }

  public void setUserID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_UserID_set(swigCPtr, this, value);
  }

  public String getUserID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_UserID_get(swigCPtr, this);
  }

  public void setSystemName(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SystemName_set(swigCPtr, this, value);
  }

  public String getSystemName() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SystemName_get(swigCPtr, this);
  }

  public void setFrontID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_FrontID_set(swigCPtr, this, value);
  }

  public int getFrontID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_FrontID_get(swigCPtr, this);
  }

  public void setSessionID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SessionID_set(swigCPtr, this, value);
  }

  public int getSessionID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SessionID_get(swigCPtr, this);
  }

  public void setMaxOrderRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_MaxOrderRef_set(swigCPtr, this, value);
  }

  public String getMaxOrderRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_MaxOrderRef_get(swigCPtr, this);
  }

  public void setSHFETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SHFETime_set(swigCPtr, this, value);
  }

  public String getSHFETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_SHFETime_get(swigCPtr, this);
  }

  public void setDCETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_DCETime_set(swigCPtr, this, value);
  }

  public String getDCETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_DCETime_get(swigCPtr, this);
  }

  public void setCZCETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_CZCETime_set(swigCPtr, this, value);
  }

  public String getCZCETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_CZCETime_get(swigCPtr, this);
  }

  public void setFFEXTime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_FFEXTime_set(swigCPtr, this, value);
  }

  public String getFFEXTime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_FFEXTime_get(swigCPtr, this);
  }

  public void setINETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_INETime_set(swigCPtr, this, value);
  }

  public String getINETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_INETime_get(swigCPtr, this);
  }

  public void setRandomString(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_RandomString_set(swigCPtr, this, value);
  }

  public String getRandomString() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcRspUserLogin2Field_RandomString_get(swigCPtr, this);
  }

  public CThostFtdcRspUserLogin2Field() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcRspUserLogin2Field(), true);
  }

}
