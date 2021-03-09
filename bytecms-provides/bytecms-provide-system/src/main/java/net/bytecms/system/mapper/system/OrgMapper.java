package net.bytecms.system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.dto.system.OrgDto;
import net.bytecms.system.entity.system.Org;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgMapper extends BaseMapper<Org> {
	
	void deleteByOrgCode(@Param("code") String code);

    List<OrgDto> getOrgsByUserId(@Param("orgCode") String orgCode);

	List<String> listUserIdsByOrgAndRole(@Param("orgIds") List<String> orgIds, @Param("roleCode") String roleCode);
}
