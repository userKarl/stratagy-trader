/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcTradingAccountPasswordUpdateField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcTradingAccountPasswordUpdateField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcTradingAccountPasswordUpdateField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcTradingAccountPasswordUpdateField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_BrokerID_get(swigCPtr, this);
  }

  public void setAccountID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_AccountID_set(swigCPtr, this, value);
  }

  public String getAccountID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_AccountID_get(swigCPtr, this);
  }

  public void setOldPassword(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_OldPassword_set(swigCPtr, this, value);
  }

  public String getOldPassword() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_OldPassword_get(swigCPtr, this);
  }

  public void setNewPassword(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_NewPassword_set(swigCPtr, this, value);
  }

  public String getNewPassword() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_NewPassword_get(swigCPtr, this);
  }

  public void setCurrencyID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_CurrencyID_set(swigCPtr, this, value);
  }

  public String getCurrencyID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcTradingAccountPasswordUpdateField_CurrencyID_get(swigCPtr, this);
  }

  public CThostFtdcTradingAccountPasswordUpdateField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcTradingAccountPasswordUpdateField(), true);
  }

}