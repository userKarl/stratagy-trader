package com.shanghaizhida.beans;

/**
 * 密保认证问题类
 *
 * Created by liwei on 2018/3/5.
 */
public class QuestionInfo implements NetParent {

    // 问题类型 0：国际期货，1：国际股票
    public String type = "";

    // 问题编号
    public String id = "";

    // 问题（中文）
    public String question = "";

    // 问题（英文）
    public String question_en = "";

    @Override
    public String MyToString() {
        String temp = this.type + "@" + this.id
                + "@" + this.question + "@" + this.question_en;

        return temp;
    }

    @Override
    public void MyReadString(String temp) {
        String[] arrClass = temp.split("@");
        this.type = arrClass[0];
        this.id = arrClass[1];
        this.question = arrClass[2];
        if (arrClass.length > 3) {
            this.question_en = arrClass[3];
		}
    }

    @Override
    public String MyPropToString() {
        String temp = "type@id@question@question_en";
        return temp;
    }
}
