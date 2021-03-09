package net.bytecms.system.service.online;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import net.bytecms.core.utils.Checker;
import net.bytecms.system.api.online.OnlineService;
import net.bytecms.system.dto.online.OnlineDto;
import net.bytecms.system.mapper.online.OnlineMapper;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.system.entity.online.Online;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-28
 */
@Service
public class OnlineServiceImpl extends BaseServiceImpl<OnlineDto, Online, OnlineMapper> implements OnlineService {

    final String suffix=".";

	@Autowired
	OnlineMapper onlineDao;

	@Resource
    DataSourceProperties dataSourceProperties;
	
	@Override
	public void initTable() {
        List<OnlineDto> onlines= listDto(new OnlineDto());
		if(null==onlines||onlines.isEmpty()){//如果数据表是空的则全部插入
			onlineDao.initTable();
		}else{//不是空的
			List<String> tableNames=getTableNames(onlines);
			List<OnlineDto> tables=onlineDao.selectTables();
			for(OnlineDto table:tables){
				if(!tableNames.contains(table.getTableName())){
				    insert(table);
				}
			}
		}
	}
	
	private List<String> getTableNames(List<OnlineDto> onlines){
		List<String> names=new ArrayList<>();
		for(OnlineDto online:onlines){
			names.add(online.getTableName());
		}
		return names;
	}

	@Override
	public void down(OnlineDto online,HttpServletRequest request) {
		    AutoGenerator mpg = new AutoGenerator();
	        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
	        mpg.setGlobalConfig(globalConfig(online));
	        mpg.setDataSource(dbConfig());
	        // 策略配置
	        StrategyConfig strategy = new StrategyConfig();
	        if(Checker.BeNotBlank(online.getPrefix())){
	        	strategy.setTablePrefix(new String[] {online.getPrefix()});// 此处可以修改为您的表前缀
	        }
	        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
	        strategy.setInclude(new String[] {online.getTableName()}); // 需要生成的表
	        strategy.setSuperEntityClass("com.org.lb.core.model.BaseModel");// 自定义实体，公共字段
	        strategy.setSuperControllerClass("com.org.lb.web.controller.BaseController");// 自定义 controller 父类
            strategy.setSuperServiceClass("com.org.lb.core.api.BaseService");
            strategy.setSuperServiceImplClass("com.org.lb.core.service.BaseServiceImpl");
	        // 生成的文件包配置
	        PackageConfig pc = new PackageConfig();
	        pc.setParent("com.org.lb");
	        //pc.setModuleName("hahahah");
            pc.setController("web.controller"+suffix+online.getModelName());
	        pc.setService("business.api"+suffix+online.getModelName());
            pc.setServiceImpl("business.service"+suffix+online.getModelName());
	        pc.setMapper("business.mapper"+suffix+online.getModelName());
	        pc.setEntity("business.entity"+suffix+online.getModelName());
	        mpg.setPackageInfo(pc);
	        myTemplate(mpg,online,request);
	         //自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
	         //放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
	         TemplateConfig tc = new TemplateConfig();
	         tc.setController("templates/online/controller.java");
	         tc.setEntity("templates/online/entity.java");
	         tc.setMapper("templates/online/mapper.java");
	         tc.setXml("templates/online/mapper.xml");
	         tc.setService("templates/online/service.java");
	         tc.setServiceImpl("templates/online/serviceImpl.java");
	         mpg.setTemplate(tc);
	         mpg.setStrategy(strategy);
	         mpg.execute();
	}

    @Override
    public void deleteByPk(Long id) {
        onlineDao.deleteById(id);
    }

    @Override
    public void updateById(OnlineDto onlineDto) {
        Online online= D2T(onlineDto);
        super.baseMapper.updateById(online);
    }


    private DataSourceConfig dbConfig(){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(dataSourceProperties.getUsername());
        dsc.setPassword(dataSourceProperties.getPassword());
        dsc.setUrl(dataSourceProperties.getUrl());
        dsc.setTypeConvert(new MySqlTypeConvert(){
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType){
                if ( fieldType.toLowerCase().contains( "timestamp" ) || fieldType.toLowerCase().contains( "datetime" )) {
                    return DbColumnType.DATE;
                }else{
                    return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                }
            }
        });
        return dsc;
    }
    
    private GlobalConfig globalConfig(OnlineDto online){
         GlobalConfig gc = new GlobalConfig();
         gc.setFileOverride(online.getFileOverride()==1);
         gc.setOutputDir(online.getLocation());
         gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
         gc.setEnableCache(false);// XML 二级缓存
         gc.setBaseResultMap(online.getResultmap()==0);// XML ResultMap
         gc.setBaseColumnList(online.getColumnList()==0);// XML columList
         //.setKotlin(true) 是否生成 kotlin 代码
         gc.setAuthor(Checker.BeNotBlank(online.getAuthor())?online.getAuthor():"LG");
         // 自定义文件命名，注意 %s 会自动填充表实体属性！
         gc.setMapperName("%sMapper");
         gc.setXmlName("%sMapper");
         gc.setServiceName("%sService");
         gc.setServiceImplName("%sServiceImpl");
         gc.setControllerName("%sController");
         gc.setOpen(false);
         return gc;
    }

    public String toLowerCases(String str){
       return str.substring(0,1).toLowerCase().concat(str.substring(1));
    }
    public String toUpperCase(String str){
        return str.substring(0,1).toUpperCase().concat(str.substring(1));
    }

//    @org.junit.jupiter.api.Test
//    public void test(){
//	    System.out.println(toLowerCases("DEpt"));
//        System.out.println("DEpt".toUpperCase());
//    }


    
    private void myTemplate(AutoGenerator mpg, OnlineDto online, HttpServletRequest request){
    	// 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("customControllerName",online.getModelName()+"Controller");
                map.put("customApiName",online.getModelName()+"Service");
                map.put("customServiceName",online.getModelName()+"ServiceImpl");
                map.put("customEntityName",online.getModelName());
                map.put("customDtoName",online.getModelName()+"Dto");
                map.put("customDtoPack",mpg.getPackageInfo().getParent()+suffix+mpg.getPackageInfo().getEntity().replace("entity","dto"));
                map.put("customMapperName",online.getModelName()+"Mapper");
                map.put("customModelName",online.getModelName());
                this.setMap(map);
            }
        };
        // 自定义 vue 模板 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/online/index.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getVuePagePosition()+File.separator+online.getModelName()+File.separator+"index.vue";
            }
        });

        // 自定义 vue 模板 生成
        focList.add(new FileOutConfig("/templates/online/handle.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getVuePagePosition()+File.separator+online.getModelName()+File.separator+"handle.vue";
            }
        });

        // 自定义 js 模板 生成
        focList.add(new FileOutConfig("/templates/online/index.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getVueJsPosition()+File.separator+online.getModelName()+File.separator+"index.js";
            }
        });
        
        //controller模板
        focList.add(new FileOutConfig("/templates/online/controller.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getControllerPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"Controller.java";
            }
        });
        //Service 模板
        focList.add(new FileOutConfig("/templates/online/serviceImpl.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getServicePosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"ServiceImpl.java";
            }
        });
        //api 模板
        focList.add(new FileOutConfig("/templates/online/service.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getApiPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"Service.java";
            }
        });

        //entity 模板
        focList.add(new FileOutConfig("/templates/online/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getEntityPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+".java";
            }
        });

        //dto 模板
        focList.add(new FileOutConfig("/templates/online/dto.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getDtoPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"Dto.java";
            }
        });

        //mapper java模板
        focList.add(new FileOutConfig("/templates/online/mapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getMapperPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"Mapper.java";
            }
        });

        //xml 模板
        focList.add(new FileOutConfig("/templates/online/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return online.getXmlPosition()+File.separator+toLowerCases(online.getModelName())+File.separator+toUpperCase(online.getModelName())+"Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    	
    }

}
