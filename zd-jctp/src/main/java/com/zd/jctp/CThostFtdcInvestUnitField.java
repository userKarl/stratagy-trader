/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcInvestUnitField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcInvestUnitField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcInvestUnitField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcInvestUnitField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_BrokerID_get(swigCPtr, this);
  }

  public void setInvestorID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorID_set(swigCPtr, this, value);
  }

  public String getInvestorID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorID_get(swigCPtr, this);
  }

  public void setInvestUnitID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestUnitID_set(swigCPtr, this, value);
  }

  public String getInvestUnitID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestUnitID_get(swigCPtr, this);
  }

  public void setInvestorUnitName(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorUnitName_set(swigCPtr, this, value);
  }

  public String getInvestorUnitName() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorUnitName_get(swigCPtr, this);
  }

  public void setInvestorGroupID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorGroupID_set(swigCPtr, this, value);
  }

  public String getInvestorGroupID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_InvestorGroupID_get(swigCPtr, this);
  }

  public void setCommModelID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_CommModelID_set(swigCPtr, this, value);
  }

  public String getCommModelID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_CommModelID_get(swigCPtr, this);
  }

  public void setMarginModelID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_MarginModelID_set(swigCPtr, this, value);
  }

  public String getMarginModelID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_MarginModelID_get(swigCPtr, this);
  }

  public void setAccountID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_AccountID_set(swigCPtr, this, value);
  }

  public String getAccountID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_AccountID_get(swigCPtr, this);
  }

  public void setCurrencyID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_CurrencyID_set(swigCPtr, this, value);
  }

  public String getCurrencyID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcInvestUnitField_CurrencyID_get(swigCPtr, this);
  }

  public CThostFtdcInvestUnitField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcInvestUnitField(), true);
  }

}
