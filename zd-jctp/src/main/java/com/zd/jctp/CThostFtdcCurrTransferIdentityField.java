/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcCurrTransferIdentityField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcCurrTransferIdentityField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcCurrTransferIdentityField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcCurrTransferIdentityField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setIdentityID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcCurrTransferIdentityField_IdentityID_set(swigCPtr, this, value);
  }

  public int getIdentityID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcCurrTransferIdentityField_IdentityID_get(swigCPtr, this);
  }

  public CThostFtdcCurrTransferIdentityField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcCurrTransferIdentityField(), true);
  }

}
