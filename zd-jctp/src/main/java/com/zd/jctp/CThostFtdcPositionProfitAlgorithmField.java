/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcPositionProfitAlgorithmField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcPositionProfitAlgorithmField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcPositionProfitAlgorithmField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcPositionProfitAlgorithmField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_BrokerID_get(swigCPtr, this);
  }

  public void setAccountID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_AccountID_set(swigCPtr, this, value);
  }

  public String getAccountID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_AccountID_get(swigCPtr, this);
  }

  public void setAlgorithm(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_Algorithm_set(swigCPtr, this, value);
  }

  public char getAlgorithm() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_Algorithm_get(swigCPtr, this);
  }

  public void setMemo(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_Memo_set(swigCPtr, this, value);
  }

  public String getMemo() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_Memo_get(swigCPtr, this);
  }

  public void setCurrencyID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_CurrencyID_set(swigCPtr, this, value);
  }

  public String getCurrencyID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcPositionProfitAlgorithmField_CurrencyID_get(swigCPtr, this);
  }

  public CThostFtdcPositionProfitAlgorithmField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcPositionProfitAlgorithmField(), true);
  }

}
