package net.bytecms.web.adapt;

import lombok.Data;

@Data
public class Coin {

    private String  faceValue; // 面值

    private String date; // 制作日期

    Coin(){

    }
    Coin(String  faceValue, String date){
        this.faceValue=faceValue;
        this.date=date;
    }
}
