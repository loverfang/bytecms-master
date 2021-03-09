package ${package.ServiceImpl};
import ${package.Entity}.${cfg.customEntityName?cap_first};
import ${superServiceImplClassPackage};
import java.util.List;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ${package.Service}.${cfg.customApiName?cap_first};
import ${package.Mapper}.${cfg.customMapperName?cap_first};
import ${cfg.customDtoPack}.${cfg.customDtoName?cap_first};
import java.util.List;
/**
 * <p>
 * ${table.comment} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Transactional
@Service
public class ${cfg.customServiceName?cap_first} extends ${superServiceImplClass}<${cfg.customDtoName?cap_first},${cfg.customEntityName?cap_first},${cfg.customMapperName?cap_first}> implements ${cfg.customApiName?cap_first} {



}
