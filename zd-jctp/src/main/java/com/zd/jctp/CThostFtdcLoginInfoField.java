/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcLoginInfoField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcLoginInfoField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcLoginInfoField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcLoginInfoField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setFrontID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_FrontID_set(swigCPtr, this, value);
  }

  public int getFrontID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_FrontID_get(swigCPtr, this);
  }

  public void setSessionID(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SessionID_set(swigCPtr, this, value);
  }

  public int getSessionID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SessionID_get(swigCPtr, this);
  }

  public void setBrokerID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_BrokerID_set(swigCPtr, this, value);
  }

  public String getBrokerID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_BrokerID_get(swigCPtr, this);
  }

  public void setUserID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_UserID_set(swigCPtr, this, value);
  }

  public String getUserID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_UserID_get(swigCPtr, this);
  }

  public void setLoginDate(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginDate_set(swigCPtr, this, value);
  }

  public String getLoginDate() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginDate_get(swigCPtr, this);
  }

  public void setLoginTime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginTime_set(swigCPtr, this, value);
  }

  public String getLoginTime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginTime_get(swigCPtr, this);
  }

  public void setIPAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_IPAddress_set(swigCPtr, this, value);
  }

  public String getIPAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_IPAddress_get(swigCPtr, this);
  }

  public void setUserProductInfo(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_UserProductInfo_set(swigCPtr, this, value);
  }

  public String getUserProductInfo() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_UserProductInfo_get(swigCPtr, this);
  }

  public void setInterfaceProductInfo(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_InterfaceProductInfo_set(swigCPtr, this, value);
  }

  public String getInterfaceProductInfo() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_InterfaceProductInfo_get(swigCPtr, this);
  }

  public void setProtocolInfo(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_ProtocolInfo_set(swigCPtr, this, value);
  }

  public String getProtocolInfo() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_ProtocolInfo_get(swigCPtr, this);
  }

  public void setSystemName(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SystemName_set(swigCPtr, this, value);
  }

  public String getSystemName() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SystemName_get(swigCPtr, this);
  }

  public void setPasswordDeprecated(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_PasswordDeprecated_set(swigCPtr, this, value);
  }

  public String getPasswordDeprecated() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_PasswordDeprecated_get(swigCPtr, this);
  }

  public void setMaxOrderRef(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_MaxOrderRef_set(swigCPtr, this, value);
  }

  public String getMaxOrderRef() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_MaxOrderRef_get(swigCPtr, this);
  }

  public void setSHFETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SHFETime_set(swigCPtr, this, value);
  }

  public String getSHFETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_SHFETime_get(swigCPtr, this);
  }

  public void setDCETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_DCETime_set(swigCPtr, this, value);
  }

  public String getDCETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_DCETime_get(swigCPtr, this);
  }

  public void setCZCETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_CZCETime_set(swigCPtr, this, value);
  }

  public String getCZCETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_CZCETime_get(swigCPtr, this);
  }

  public void setFFEXTime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_FFEXTime_set(swigCPtr, this, value);
  }

  public String getFFEXTime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_FFEXTime_get(swigCPtr, this);
  }

  public void setMacAddress(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_MacAddress_set(swigCPtr, this, value);
  }

  public String getMacAddress() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_MacAddress_get(swigCPtr, this);
  }

  public void setOneTimePassword(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_OneTimePassword_set(swigCPtr, this, value);
  }

  public String getOneTimePassword() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_OneTimePassword_get(swigCPtr, this);
  }

  public void setINETime(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_INETime_set(swigCPtr, this, value);
  }

  public String getINETime() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_INETime_get(swigCPtr, this);
  }

  public void setIsQryControl(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_IsQryControl_set(swigCPtr, this, value);
  }

  public int getIsQryControl() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_IsQryControl_get(swigCPtr, this);
  }

  public void setLoginRemark(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginRemark_set(swigCPtr, this, value);
  }

  public String getLoginRemark() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_LoginRemark_get(swigCPtr, this);
  }

  public void setPassword(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_Password_set(swigCPtr, this, value);
  }

  public String getPassword() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcLoginInfoField_Password_get(swigCPtr, this);
  }

  public CThostFtdcLoginInfoField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcLoginInfoField(), true);
  }

}
