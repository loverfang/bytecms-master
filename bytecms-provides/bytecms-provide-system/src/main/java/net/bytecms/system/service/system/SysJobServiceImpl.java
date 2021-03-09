package net.bytecms.system.service.system;

import net.bytecms.core.handler.CustomException;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.JobUtil;
import net.bytecms.system.api.system.SysJobService;
import net.bytecms.system.dto.system.SysJobDto;
import net.bytecms.system.entity.system.SysJob;
import net.bytecms.system.mapper.system.SysJobMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-19
 */
@Service
public class SysJobServiceImpl extends BaseServiceImpl<SysJobDto, SysJob,SysJobMapper> implements SysJobService {

    @Autowired
    Scheduler scheduler;

    @Transactional
    @Override
    public void taskAction(SysJobDto v) {
        SysJobDto sysJobDto = super.getByPk(v.getId());
        String jobName = sysJobDto.getJobName();
        String jobGroup = sysJobDto.getJobGroup();
        String cron = sysJobDto.getCronExpression();
        String param = sysJobDto.getJobParam();
        Map<String,Object> jobParams =null;
        if(Checker.BeNotBlank(param)){
            jobParams = new HashMap<>();
            jobParams.put("param",param);
        }
        String jobBeanClass = sysJobDto.getJobClass();
        String action = v.getAction();
        try {
            Class<?> jobClass = Class.forName(jobBeanClass);
            switch (action){
                case "0":
                    if(Checker.BeNotNull(JobUtil.ckJobIsExist(scheduler,jobName,jobGroup))){
                        JobUtil.restartOneJob(scheduler,jobName,jobGroup);
                    }else{
                        JobUtil.createJobByCron(scheduler,jobName,jobGroup,cron,jobClass,jobParams);
                    }
                    break;
                case "1":
                    JobUtil.pasueOneJob(scheduler,jobName,jobGroup);
                    break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new CustomException(ApiResult.result(20008));
        }
        sysJobDto.setJobStatus(action);
        super.updateByPk(sysJobDto);
    }


    @Override
    @Transactional
    public boolean deleteByPk(String id){
        SysJobDto sysJobDto = super.getByPk(id);
        String jobName = sysJobDto.getJobName();
        String jobGroup = sysJobDto.getJobGroup();
        try {
            JobUtil.removeJob(scheduler,jobName,jobGroup);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw  new CustomException(ApiResult.result(20010));
        }
        return super.deleteByPk(id);
    }

    @Transactional
   public boolean updateByPk(SysJobDto dto){
       SysJobDto sysJobDto =getByPk(dto.getId());
       if(!sysJobDto.getJobStatus().equals("-1")){ // 为启动时不做处理
           String jobName = sysJobDto.getJobName();
           String jobGroup = sysJobDto.getJobGroup();
           if(!sysJobDto.getCronExpression().equals(dto.getCronExpression())){// 修改时间
               try {
                   JobUtil.modifyJobTime(scheduler,jobName,jobGroup,dto.getCronExpression());
               }catch (RuntimeException e){
                   throw new CustomException(ApiResult.result(20011));
               }
           }
           if(Checker.BeNotBlank(dto.getJobParam())){ //修改参数
               if(!dto.getJobParam().equals(sysJobDto.getJobParam())){
                   Map<String,Object> jobParams  = new HashMap<>();
                   jobParams.put("param",dto.getJobParam());
                   try {
                       JobUtil.modifyJobParam(scheduler,jobName,jobGroup,null,jobParams);
                   }catch (RuntimeException e){
                       throw new CustomException(ApiResult.result(20011));
                   }
               }
           }
       }
       return super.updateByPk(dto) ;
   }
}
