package com.shanghaizhida.beans;

/**
 * Created by olq on 2017-10-18.
 * 成交明细
 */

public class TransactionDetailInfo implements NetParent{

    /**
     * 时间
     */
    private String tradeDate;
    /**
     * 价格
     */
    private String price;
    /**
     * 成交量
     */
    private String tradeVol;
    /**
     * 昨收、昨结价
     */
    private String oldClose;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTradeVol() {
        return tradeVol;
    }

    public void setTradeVol(String tradeVol) {
        this.tradeVol = tradeVol;
    }

    public String getOldClose() {
        return oldClose;
    }

    public void setOldClose(String oldClose) {
        this.oldClose = oldClose;
    }


    @Override
    public String MyToString() {
        return null;
    }

    @Override
    public void MyReadString(String temp) {
//        System.out.println(temp);
        String[] arrClass = temp.split(",");
        this.tradeDate = arrClass[0];
        this.price = arrClass[1];
        this.tradeVol = arrClass[2];

    }

    @Override
    public String MyPropToString() {
        return null;
    }

}
