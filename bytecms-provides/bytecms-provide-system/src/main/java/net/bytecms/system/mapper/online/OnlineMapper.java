package net.bytecms.system.mapper.online;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.system.dto.online.OnlineDto;
import net.bytecms.system.entity.online.Online;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dl
 * @since 2018-03-28
 */
@Mapper
@Repository
public interface OnlineMapper extends BaseMapper<Online> {

    @Insert("insert into sys_online(table_name,table_note,gmt_create,`engine`)"
    		+ "select table_name , table_comment table_note, create_time gmt_create, engine from information_schema.tables "
    		+ "where table_schema = (select database()) and table_name not in ('sys_online')")
	void initTable();

    @Select(" select table_name tableName, engine, table_comment tableNote, create_time gmtCreate from information_schema.tables "
    		+ "where table_schema = (select database()) and table_name not in ('sys_online')")
    List<OnlineDto> selectTables();
}
