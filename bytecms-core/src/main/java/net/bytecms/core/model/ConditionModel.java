package net.bytecms.core.model;


import net.bytecms.core.enumerate.ConditionType;
import lombok.Data;

import java.util.Arrays;

@Data
public class ConditionModel<M extends BaseModel<M>> extends  AbsConditionModel{

    public ConditionModel(){

    }
    private SqlResolverFactory sqlResolverFactory;

    public ConditionModel(BaseModel m){
        this.sqlResolverFactory=new SqlResolver();
        m.setCondition(this);
    }



    protected static <M> ConditionModel build(M m) {
       ConditionModel condition=new ConditionModel((BaseModel)m);
       return condition;
    }


    public ConditionModel select(String ... fields){
        if(fields.length>0){
            selectField.addAll(Arrays.asList(fields));
        }
        return this;
    }

    public ConditionModel orderByAsc(String ... fields){
        if(fields.length>0){
            ascField.addAll(Arrays.asList(fields));
        }
        return this;
    }

    public ConditionModel orderByDesc(String ... fields){
        if(fields.length>0){
            descField.addAll(Arrays.asList(fields));
        }
        return this;
    }

    @Override
    public AbsConditionModel eq(String field, String value) {
        sqlResolverFactory.build(ConditionType.EQ,field,value);
        return this;
    }

    @Override
    public AbsConditionModel like(String field, String value) {
        sqlResolverFactory.build(ConditionType.LIKE,field,value);
        return this;
    }

    @Override
    public AbsConditionModel notLike(String field, String value) {
        sqlResolverFactory.build(ConditionType.NOT_LIKE,field,value);
        return this;
    }

    @Override
    public AbsConditionModel notIn(String field, String value) {
        sqlResolverFactory.build(ConditionType.NOT_IN,field,value);
        return this;
    }

    @Override
    public AbsConditionModel in(String field, String ... value) {
        sqlResolverFactory.build(ConditionType.IN,field,value);
        return this;
    }
}
