package net.bytecms.core.model;

import net.bytecms.core.enumerate.ConditionType;
import net.bytecms.core.utils.Checker;
import lombok.Data;

import java.util.Map;

@Data
public class SqlResolver extends SqlResolverFactory {

    @Override
    protected Map<String, Object> initParam(ConditionType conditionType) {
        switch (conditionType) {
            case EQ:
                EQ = initMap(EQ);
                return EQ;
            case NOT_IN:
                NOTIN = initMap(NOTIN);
                return NOTIN;
            case LIKE:
                LIKE = initMap(LIKE);
                return LIKE;
            case IN:
                IN = initMap(IN);
                return IN;
            case NOT_LIKE:
                NOTLIKE = initMap(NOTLIKE);
                return NOTLIKE;
		default:
			break;
        }
        return null;
    }

    @Override
    public void build(ConditionType conditionType, String field, Object value) {
        if(Checker.BeNotNull(value)&&Checker.BeNotNull(field))
        initParam(conditionType).put(field, value);
    }
}
