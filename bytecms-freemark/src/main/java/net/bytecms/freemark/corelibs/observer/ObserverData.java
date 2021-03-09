package net.bytecms.freemark.corelibs.observer;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.model.BaseModel;
import net.bytecms.core.model.PageKeyWord;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public abstract class ObserverData extends BaseModel {

    private Map<String,Object> mapData=new LinkedHashMap<>(16);

    private ObserverAction observerAction=ObserverAction.DEFAULT_GEN;

    private PageKeyWord pageKeyWord;

    public ObserverData(ObserverAction observerAction){
        this.observerAction = observerAction;
    }

    public String getDateStr(){
        return DateTimeFormatter.ofPattern(Constants.YMD_).format(LocalDateTime.now());
    }

}
