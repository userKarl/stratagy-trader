/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcCommRateModelField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcCommRateModelField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcCommRateModelField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcCommRateModelField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_BrokerID_get(swigCPtr, this);
  }

  public void setCommModelID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_CommModelID_set(swigCPtr, this, value);
  }

  public String getCommModelID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_CommModelID_get(swigCPtr, this);
  }

  public void setCommModelName(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_CommModelName_set(swigCPtr, this, value);
  }

  public String getCommModelName() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcCommRateModelField_CommModelName_get(swigCPtr, this);
  }

  public CThostFtdcCommRateModelField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcCommRateModelField(), true);
  }

}
