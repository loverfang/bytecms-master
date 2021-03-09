package net.bytecms.core.api;

import net.bytecms.core.model.PageDto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface BaseService<D> {

	/**
	 * 不分页查询list
	 * @param d
	 * @return
	 */
	List<D> listDto(D d);

	/**
	 * 根据 map 查询 list
	 * @param map
	 * @return
	 */
	List<D> listDtoByMap(Map<String, Object> map);
	
	
	/**分页查询
	 * @param d
	 * @return
	 */
	PageDto<D> listPage(PageDto<D> d);
	
	
	/**保存对象dto
	 * @param d
	 * @return
	 */
	boolean insert(D d);

	/**
	 * 批量插入
	 * @param dtos
	 * @return
	 */
	boolean insertBatch(List<D> dtos);

	/**
	 * 更新对象
	 * @param d
	 * @return
	 */

	boolean updateByPk(D d);

	/**
	 * 批量更新
	 * @param dtos
	 * @return
	 */
    boolean updateByPks(List<D> dtos);


	/**
	 * 根据特定字段更新
	 * @param field
	 * @param val
	 * @param d
	 * @return
	 */

	boolean updateByField(String field,String val,D d);

	/**
	 * 根据主键查询
	 * @param pk
	 * @return
	 */
	D getByPk(Serializable pk);

	List<D> getByPks(Collection<String> pks);
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */

	boolean deleteByPk(String id);


	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	boolean deleteByPks(Collection<String> ids);


	/**
	 * 根据某个字段删除
	 * @param field
	 * @param val
	 * @return
	 */
	boolean deleteByFiled(String field,Object val);


	/**
	 * 根据条件查询单个dto
	 * @param d
	 * @return
	 */
	D getOneDto(D d);

	/**
	 * 根据map查询单个对象，不推荐使用
	 * @param param
	 * @return
	 */
	D getOneByMap(Map<String, Object> param);

	/**
	 * 根据map删除
	 * @param param
	 * @return
	 */
    boolean removeByMap(Map<String, Object> param);

	/**
	 * 根据指定字段查询
	 * @param field
	 * @param val
	 * @return
	 */
    D getByField(String field,String val);


	/**
	 * 根据指定字段查询list
	 * @param field
	 * @param val
	 * @return
	 */
	List<D> listByField(String field,Object val);


	List<D> listByField(String field,Object val,String[] orderByDesc,String[] orderByAsc);
}
