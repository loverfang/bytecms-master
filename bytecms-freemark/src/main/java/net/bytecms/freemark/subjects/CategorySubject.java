package net.bytecms.freemark.subjects;

import net.bytecms.freemark.corelibs.subject.SubjectAdapt;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CategorySubject extends SubjectAdapt {



// 系统默认提供消息通知，可以重写该通知
//    @Override
//    public void callBack(ObserverData observerData) {
//      if(Checker.BeNotNull(observerData)){
//          ObserverAction action=observerData.getObserverAction();
//          if(Checker.BeNotNull(action)){
//              action.getAction();
//          }
//      }
//    }
}
