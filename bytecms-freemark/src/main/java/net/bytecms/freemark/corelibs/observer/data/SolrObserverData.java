package net.bytecms.freemark.corelibs.observer.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.freemark.corelibs.observer.ObserverAction;
import net.bytecms.freemark.corelibs.observer.ObserverData;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolrObserverData extends ObserverData {

    public SolrObserverData(ObserverAction observerAction){
        super(observerAction);
    }

}
