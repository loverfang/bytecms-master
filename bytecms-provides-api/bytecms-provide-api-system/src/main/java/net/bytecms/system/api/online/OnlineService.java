package net.bytecms.system.api.online;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.online.OnlineDto;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dl
 * @since 2018-03-28
 */
public interface OnlineService extends BaseService<OnlineDto> {
	/** 
	* @Description: 初始化table
	* @param   
	* @return void 
	* @throws 
	*/ 
	public void initTable();
	
	public void down(OnlineDto online, HttpServletRequest request);

	void deleteByPk(Long id);

	void updateById(OnlineDto online);
}
