/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.zd.jctp;

public class CThostFtdcProductField {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CThostFtdcProductField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcProductField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        jctpmdapiv6v3v11x64JNI.delete_CThostFtdcProductField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setProductID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductID_set(swigCPtr, this, value);
  }

  public String getProductID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductID_get(swigCPtr, this);
  }

  public void setProductName(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductName_set(swigCPtr, this, value);
  }

  public String getProductName() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductName_get(swigCPtr, this);
  }

  public void setExchangeID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ExchangeID_set(swigCPtr, this, value);
  }

  public String getExchangeID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ExchangeID_get(swigCPtr, this);
  }

  public void setProductClass(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductClass_set(swigCPtr, this, value);
  }

  public char getProductClass() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ProductClass_get(swigCPtr, this);
  }

  public void setVolumeMultiple(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_VolumeMultiple_set(swigCPtr, this, value);
  }

  public int getVolumeMultiple() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_VolumeMultiple_get(swigCPtr, this);
  }

  public void setPriceTick(double value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PriceTick_set(swigCPtr, this, value);
  }

  public double getPriceTick() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PriceTick_get(swigCPtr, this);
  }

  public void setMaxMarketOrderVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MaxMarketOrderVolume_set(swigCPtr, this, value);
  }

  public int getMaxMarketOrderVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MaxMarketOrderVolume_get(swigCPtr, this);
  }

  public void setMinMarketOrderVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MinMarketOrderVolume_set(swigCPtr, this, value);
  }

  public int getMinMarketOrderVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MinMarketOrderVolume_get(swigCPtr, this);
  }

  public void setMaxLimitOrderVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MaxLimitOrderVolume_set(swigCPtr, this, value);
  }

  public int getMaxLimitOrderVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MaxLimitOrderVolume_get(swigCPtr, this);
  }

  public void setMinLimitOrderVolume(int value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MinLimitOrderVolume_set(swigCPtr, this, value);
  }

  public int getMinLimitOrderVolume() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MinLimitOrderVolume_get(swigCPtr, this);
  }

  public void setPositionType(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PositionType_set(swigCPtr, this, value);
  }

  public char getPositionType() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PositionType_get(swigCPtr, this);
  }

  public void setPositionDateType(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PositionDateType_set(swigCPtr, this, value);
  }

  public char getPositionDateType() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_PositionDateType_get(swigCPtr, this);
  }

  public void setCloseDealType(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_CloseDealType_set(swigCPtr, this, value);
  }

  public char getCloseDealType() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_CloseDealType_get(swigCPtr, this);
  }

  public void setTradeCurrencyID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_TradeCurrencyID_set(swigCPtr, this, value);
  }

  public String getTradeCurrencyID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_TradeCurrencyID_get(swigCPtr, this);
  }

  public void setMortgageFundUseRange(char value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MortgageFundUseRange_set(swigCPtr, this, value);
  }

  public char getMortgageFundUseRange() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_MortgageFundUseRange_get(swigCPtr, this);
  }

  public void setExchangeProductID(String value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ExchangeProductID_set(swigCPtr, this, value);
  }

  public String getExchangeProductID() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_ExchangeProductID_get(swigCPtr, this);
  }

  public void setUnderlyingMultiple(double value) {
    jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_UnderlyingMultiple_set(swigCPtr, this, value);
  }

  public double getUnderlyingMultiple() {
    return jctpmdapiv6v3v11x64JNI.CThostFtdcProductField_UnderlyingMultiple_get(swigCPtr, this);
  }

  public CThostFtdcProductField() {
    this(jctpmdapiv6v3v11x64JNI.new_CThostFtdcProductField(), true);
  }

}
