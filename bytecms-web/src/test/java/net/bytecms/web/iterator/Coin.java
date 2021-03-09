package net.bytecms.web.iterator;

import lombok.Data;

@Data
public class Coin {

    private Integer  faceValue; // 面值

    private String unit; // 制作日期

    Coin(Integer  faceValue,String unit){
        this.faceValue=faceValue;
        this.unit=unit;
    }
}
