package net.bytecms.core.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.BaseModel;
import net.bytecms.core.model.ConditionModel;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SqlResolverFactory;
import net.bytecms.core.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl<D extends BaseModel,T extends BaseModel,M extends BaseMapper<T>> extends ServiceImpl<M, T> implements BaseService<D> {

    @Autowired
    Mapper dozerMapper;

	protected Class<T> tclz;

    protected Class<D> dclz;

    protected List<String> select(){ return null; };

	protected String getUserId(){
		return BaseContextKit.getUserId();
	}

	protected String getOrgId(){
		return BaseContextKit.getDeptId();
	}

	protected String getUserName(){
		return BaseContextKit.getUserName();
	}

	protected List<String> getRoleSigns(){
		return BaseContextKit.getRoleSigns();
	}

	protected String getOpenId(){
		return BaseContextKit.getOpenId();
	}

	protected List<D> T2DList(List<T> list){
		return DozerUtils.T2DList(dozerMapper,list,dclz);
	}

	protected List<T> D2TList(List<D> list){
		return DozerUtils.D2TList(dozerMapper,list,tclz);
	}

	protected List<D> ResultT2D(List<T> list){
		return Checker.BeNotEmpty(list) ? T2DList(list):Lists.newArrayList();
	}


	protected String generateId(){
    	return SnowflakeIdWorker.getId();
	}

    public BaseServiceImpl(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        dclz = (Class<D>) params[0];
        tclz = (Class<T>) params[1];
    }

	protected T D2T(D d){
		return dozerMapper.map(d, tclz);
	}

	protected D T2D(T t){
		return dozerMapper.map(t, dclz);
	}

	@Override
	public List<D> listDto(D d) {
        T t=D2T(d);
        QueryWrapper<T> queryWrapper= wrapperIt(t,d);
        List<T> list=super.list(queryWrapper);
        if (Checker.BeEmpty(list)) {
			return Lists.newArrayList();
		}
        return DozerUtils.T2DList(dozerMapper,list,dclz);
	}

	@Override
	public List<D> listDtoByMap(Map<String, Object> map) {
    	List<T> list= (List<T>) super.listByMap(map);
		if (Checker.BeEmpty(list)) {
			return Lists.newArrayList();
		}
		return DozerUtils.T2DList(dozerMapper,list,dclz);
	}

	@Override
	public PageDto<D> listPage(PageDto<D> d) {
        T t=D2T(d.getDto());
        QueryWrapper<T> queryWrapper= wrapperIt(t,d.getDto());
        Page<T> page = new Page<T>(d.getPageNo(),d.getPageSize());
        IPage<T> pageRes = super.page(page, queryWrapper);
        if(Checker.BeEmpty(pageRes.getRecords())){
            return new PageDto<>(0,Lists.newArrayList());
        }
        List<D> dlist =DozerUtils.T2DList(dozerMapper,pageRes.getRecords(),dclz);
        long count = pageRes.getTotal();
        return  new PageDto<>(count,pageRes.getPages(),pageRes.getCurrent(),dlist);
	}

	@Override
	@Transactional
	public boolean insert(D d) {
        Date date=new Date();
        T t=D2T(d);
		String pk=Checker.BeNotBlank(t.getId())?t.getId(): generateId();
        t.setGmtCreate(date).setGmtModified(date).setCreateId(getUserId()).
        setId(Checker.BeNotBlank(t.getId())?t.getId(): generateId());
        return super.save(t);
	}

	@Override
	public boolean insertBatch(List<D> dtos) {
		List<T> dlist =DozerUtils.D2TList(dozerMapper,dtos,tclz);
		Date date=new Date();
		if(Checker.BeNotEmpty(dlist)){
			dlist.forEach(dclz->{
				dclz.setGmtCreate(date).setCreateId(getUserId());
			});
		}
		return super.saveBatch(dlist);
	}

	@Override
	@Transactional
	@CacheEvict(value = Constants.cacheName, key = "#root.targetClass+'.getByPk.'+#p0.id")
	public boolean updateByPk(D d) {
        Date date=new Date();
        T t=D2T(d);
		checkPkIsNull(t);
        t.setGmtModified(date).setModifiedId(getUserId());
		return super.updateById(t);
	}

	@Override
	public boolean updateByPks(List<D> dtos) {
    	if(Checker.BeNotEmpty(dtos)){
			dtos.forEach(d -> {
				this.updateByPk(d);
			});
			return true;
		}
		return false;
	}

	@Override
	public boolean updateByField(String field,String val, D dval) {
		T t =D2T(dval);
		QueryWrapper<T> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq(field,val);
		return update(t,queryWrapper);
	}

	@Override
	@Cacheable(value=Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#root.args[0]",unless="#result == null")
	public D getByPk(Serializable pk) {
         T t=super.getById(pk);
         return Checker.BeNotNull(t)?T2D(t):null;
    }

	@Override
	public List<D> getByPks(Collection<String> pks) {
		if(Checker.BeNotEmpty(pks)){
			List<T> ts = (List<T>) listByIds(pks);
			return Checker.BeNotEmpty(ts)? T2DList(ts): Lists.newArrayList();
		}
		return Lists.newArrayList();
	}

	@Override
	@Transactional
	@CacheEvict(value = Constants.cacheName, key = "#root.targetClass+'.getByPk.'+#root.args[0]")
	public boolean deleteByPk(String id) {
		return super.removeById(id);
	}

	@Override
	public boolean deleteByPks(Collection<String> ids) {
		return super.removeByIds(ids);
	}

	@Override
	public boolean deleteByFiled(String field, Object val) {
		if(Checker.BeNotBlank(field) && Checker.BeNotNull(val)){
			QueryWrapper<T> queryWrapper=  new QueryWrapper<>();
			if(val instanceof String){
				queryWrapper.eq(field,val);
			}
			if(val instanceof Collection){
				queryWrapper.in(field,((Collection)val).toArray());
			}
			super.remove(queryWrapper);
		}
		return false;
	}

	@Override
	public D getOneDto(D d) {
        QueryWrapper<T> queryWrapper= wrapperIt(D2T(d),d);
        T t=super.getOne(queryWrapper);
        return Checker.BeNotNull(t)?T2D(t):null;
	}

	@Override
	public D getOneByMap(Map<String, Object> param) {
		List<D> dtos=listDtoByMap(param);
		return Checker.BeNotEmpty(dtos)?dtos.get(0):null;
	}

	@Override
	public D getByField(String field, String val) {
		QueryWrapper<T> queryWrapper= new QueryWrapper<>();
		queryWrapper.eq(field,val);
		T t=super.getOne(queryWrapper);
		return Checker.BeNotNull(t)?T2D(t):null;
	}

	@Override
	public List<D> listByField(String field, Object val,String[] orderByDesc,String[] orderByAsc) {
		QueryWrapper<T> queryWrapper= new QueryWrapper<>();
		if(Checker.BeNotEmpty(orderByDesc)){
			queryWrapper.orderByDesc(orderByDesc);
		}
		if(Checker.BeNotEmpty(orderByAsc)){
			queryWrapper.orderByAsc(orderByAsc);
		}
		if(val instanceof String)queryWrapper.eq(field,val);
		if(val instanceof Collection) {
			if(Checker.BeNotEmpty((Collection)val)){
				queryWrapper.in(field,((Collection)val).toArray());
			}else{
				return Lists.newArrayList();
			}
		}
		List<T> ts=super.list(queryWrapper);
		return Checker.BeNotEmpty(ts)?T2DList(ts):Lists.newArrayList();
	}


	@Override
	public List<D> listByField(String field, Object val) {
		QueryWrapper<T> queryWrapper= new QueryWrapper<>();
		if(val instanceof String){
			queryWrapper.eq(field,val);
		}
		if(val instanceof Collection) {
			if(Checker.BeNotEmpty((Collection)val)){
				queryWrapper.in(field,((Collection)val).toArray());
			}else{
				return Lists.newArrayList();
			}
		}
		List<T> ts=super.list(queryWrapper);
		return Checker.BeNotEmpty(ts)?T2DList(ts):Lists.newArrayList();
	}


	private void checkPkIsNull(T t){
		 if(Checker.BeBlank(t.getId())){
	      throw new CustomException(ApiResult.result(4001));
		 }
	}

	private QueryWrapper<T> wrapperIt(T t,D d) {
		if (Checker.BeNull(t)) {
			return new QueryWrapper<>(t);
		}
		QueryWrapper<T> wrapper = null;
		wrapper = condition(d);
		if (Checker.BeNull(wrapper)) {
			wrapper = new QueryWrapper<T>();
			autoCondition(wrapper,t);
		}
		return wrapper;
	}


	private QueryWrapper<T> condition(D d) {
		ConditionModel condition=d.getCondition();
		if(Checker.BeNotNull(condition)){
			QueryWrapper<T> wrapper=new QueryWrapper<T>();
			if(Checker.BeNotEmpty(condition.getSelectField())){
				wrapper.select(StringUtils.join(condition.getSelectField().toArray(),","));
			}
			if(Checker.BeNotEmpty(condition.getAscField())){
				wrapper.orderByAsc(StringUtils.join(condition.getAscField().toArray(),","));
			}
			if(Checker.BeNotEmpty(condition.getDescField())){
				wrapper.orderByDesc(StringUtils.join(condition.getDescField().toArray(),","));
			}
			if(Checker.BeNotNull(condition.getSqlResolverFactory())){
				resolverSqlTemplate(wrapper,condition.getSqlResolverFactory());
			}
			return wrapper;
		}
		return null;
	};

	private void resolverSqlTemplate(QueryWrapper<T> wrapper, SqlResolverFactory sqlResolverFactory){
		if(Checker.BeNotNull(sqlResolverFactory)){
			resolverSql(wrapper,sqlResolverFactory);
		}
	}
	private void resolverSql(QueryWrapper<T> wrapper,SqlResolverFactory sqlResolverFactory){
         if(Checker.BeNotEmpty(sqlResolverFactory.EQ)){
			 sqlResolverFactory.EQ.forEach((k,v)->{
				 wrapper.eq(k,v);
			 });
		 }
		 if(Checker.BeNotEmpty(sqlResolverFactory.NOTIN)){
			 sqlResolverFactory.NOTIN.forEach((k,v)->{
				 wrapper.notIn(k,v);
			 });
		 }
		 if(Checker.BeNotEmpty(sqlResolverFactory.LIKE)){
			 sqlResolverFactory.LIKE.forEach((k,v)->{
				 wrapper.like(k,v);
			 });
		 }
		 if(Checker.BeNotEmpty(sqlResolverFactory.NOTLIKE)){
			 sqlResolverFactory.NOTLIKE.forEach((k,v)->{
				 wrapper.notLike(k,v);
			 });
		 }
		if(Checker.BeNotEmpty(sqlResolverFactory.IN)){
			sqlResolverFactory.IN.forEach((k,v)->{
				wrapper.in(k,(String[] )v);
			});
		}
	}

	private void autoCondition(QueryWrapper<T>  wrapper,T t){
		Field[] files=t.getClass().getDeclaredFields();
	    for(Field file:files){
			try {
				 file.setAccessible(true);
				 if(Checker.BeNotNull(file.get(t))&&Checker.BeNotBlank(file.get(t).toString())){
					 TableField tableFile=file.getAnnotation(TableField.class);
					 TableId tableId=file.getAnnotation(TableId.class);
					 if(Checker.BeNotNull(tableFile)&&!tableFile.exist()){//忽略字段
						 continue;
					 }
					 if(Checker.BeNotNull(tableId)){//主键
						 wrapper.eq((Checker.BeBlank(tableId.value())?file.getName():tableId.value()), file.get(t));
					 }else{//非主键的情况
					 	 if(Checker.BeNotNull(tableFile)){
							 if(String.class.isAssignableFrom(file.getType())){
								 wrapper.like((Checker.BeNotBlank(tableFile.value())?tableFile.value():file.getName()),file.get(t).toString());
							 }
							 if(Number.class.isAssignableFrom(file.getType())){
								 wrapper.eq((Checker.BeNotBlank(tableFile.value())?tableFile.value():file.getName()), file.get(t));
							 }
						 }
					 }
				 }
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
	    }
	}

}
