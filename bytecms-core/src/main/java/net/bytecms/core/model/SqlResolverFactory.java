package net.bytecms.core.model;

import com.google.common.collect.Maps;
import net.bytecms.core.enumerate.ConditionType;
import net.bytecms.core.utils.Checker;
import lombok.Data;

import java.util.Map;

@Data
public class SqlResolverFactory{

    public SqlResolverFactory() {

    }
    public Map<String, Object> EQ;
    public Map<String, Object> IN;
    public Map<String, Object> NOTIN;
    public Map<String, Object> LIKE;
    public Map<String, Object> NOTLIKE;


    protected  Map<String, Object> initParam( ConditionType conditionType){
        return null;
    };

    public  void build(ConditionType conditionType, String field, Object value){

    };

    public Map<String, Object> initMap(Map<String, Object> param){
        return Checker.BeNotNull(param)?param:Maps.newHashMap();
    }
}
