package net.bytecms.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbsConditionModel {


    public AbsConditionModel(){

    }

    public List<String> selectField=new ArrayList<>();

    public List<String> ascField=new ArrayList<>();

    public List<String> descField=new ArrayList<>();

    public abstract AbsConditionModel eq(String field,String value);

    public abstract AbsConditionModel like(String field,String value);

    public abstract AbsConditionModel notLike(String field,String value);

    public abstract AbsConditionModel notIn(String field,String value);

    public abstract AbsConditionModel in(String field,String... value);

}
