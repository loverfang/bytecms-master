package net.bytecms.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JobUtil {

    /**
     * 任务创建
     * @param scheduler
     * @param cron
     * @param startAtTime
     * @param endAtTime
     * @param name
     * @param group
     * @param jobBean
     */
    public static void createJobByStartEndAt(Scheduler scheduler, String cron, Date startAtTime,Date endAtTime, String name, String group,Map<String,Object> params, Class jobBean) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartEndAt  start");
        Trigger trigger =ckJobIsExist(scheduler,name,group);
        if(trigger == null){
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(startAtTime).endAt(endAtTime).
            withSchedule(scheduleBuilder).build();
            if(Checker.BeNotNull(params) && !params.isEmpty()){
                trigger.getJobDataMap().putAll(params);
            }
            createJob(scheduler, name, group, trigger, jobBean);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartAt  end");
    }



    /**
     * @param scheduler   quartz调度器
     * @param startAtTime 任务执行时刻
     * @param name        任务名称
     * @param group       任务组名称
     * @param jobBean     具体任务
     */
    public static void createJobByStartAt(Scheduler scheduler, long startAtTime, String name, String group, Class jobBean) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartAt  start");
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startAtTime)).build();
        createJob(scheduler, name, group, trigger, jobBean);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartAt  end");
    }

    public static void createJobByStartAt(Scheduler scheduler, long startAtTime, String name, String group, Class jobBean, Map<String,Object> param) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartAt  start");
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startAtTime)).build();
        if(param!=null && !param.isEmpty()){
            trigger.getJobDataMap().putAll(param);
        }
        createJob(scheduler, name, group, trigger, jobBean);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByStartAt  end");
    }

    /**
     * @param scheduler quartz调度器
     * @param name      任务名称
     * @param group     任务组名称
     * @param cron      cron表达式
     * @param jobBean   具体任务
     */
    public static void createJobByCron(Scheduler scheduler, String name, String group, String cron, Class jobBean) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByCron  start");
        Trigger trigger =ckJobIsExist(scheduler,name,group);
        if(trigger == null){
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            //创建任务触发器
            trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            createJob(scheduler, name, group, trigger, jobBean);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByCron  end");
    }



    /**
     * @param scheduler quartz调度器
     * @param name      任务名称
     * @param group     任务组名称
     * @param cron      cron表达式
     * @param jobBean   具体任务
     */
    public static void createJobByCron(Scheduler scheduler, String name, String group, String cron, Class jobBean,Map<String ,Object> param) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByCron  start");
        Trigger trigger =ckJobIsExist(scheduler,name,group);
        if(trigger == null){
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            //创建任务触发器
            trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            createJob(scheduler, name, group, trigger, jobBean,param);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:createJobByCron  end");
    }

    /**
     * 修改任务时间
     * @param scheduler
     * @param name
     * @param group
     * @param cron
     */
    public static Trigger modifyJobTime(Scheduler scheduler, String name, String group, String cron) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobTime  start");
        Trigger trigger=null;
        try {
            // 唯一主键
            trigger = ckJobIsExist(scheduler, name, group);
            if (trigger != null) {
                TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
                CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
                scheduler.rescheduleJob(triggerKey, cronTrigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobTime  end");
        return trigger;
    }


    /**
     * 修改任务触发器参数
     */
    public static void modifyJobParam(Scheduler scheduler, Trigger trigger,Map<String,Object> params) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobParam  start");
        try {
            // 唯一主键
            if (trigger != null) {
                TriggerKey triggerKey = trigger.getKey();
                trigger.getJobDataMap().putAll(params);
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobParam  end");
    }

    /**
     * 修改任务触发器参数
     */
    public static void modifyJobParam(Scheduler scheduler, String name, String group, Map<String,Object> triggerParams, Map<String,Object> jobParams) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobParam  start");
        Trigger trigger = ckJobIsExist(scheduler, name, group);
        if(trigger!=null){
            if(triggerParams!=null&&!triggerParams.isEmpty()){
                try {
                    modifyJobParam(scheduler,trigger,triggerParams);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }if(jobParams!=null&&!jobParams.isEmpty()){
                JobDetail jobDetail= ckJobDetailIsExist(scheduler,trigger);
                if(jobDetail!=null){
                    jobDetail.getJobDataMap().putAll(jobParams);
                    try {
                        scheduler.deleteJob(jobDetail.getKey());
                        scheduler.scheduleJob(jobDetail,trigger);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:modifyJobParam  end");
        }

    /**
     * 重启一个任务
     * @param scheduler
     * @param name
     * @param group
     */
    public static void restartOneJob(Scheduler scheduler, String name, String group) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:resOneJob  start");
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            Trigger trigger = ckJobIsExist(scheduler, name, group);
            if(trigger !=null){
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:resOneJob  end");
    }


    /**
     * 暂停一个任务
     * @param scheduler
     * @param name
     * @param group
     */
    public static void pasueOneJob(Scheduler scheduler, String name, String group) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pasueOneJob  start");
        try {
            Trigger trigger = ckJobIsExist(scheduler, name, group);
            if(trigger!=null){
                JobKey jobKey = trigger.getJobKey();
                scheduler.pauseJob(jobKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>method:pasueOneJob  end");
    }


    /**
     * // 启动/暂停全部任务
     * @param scheduler
     * @param start
     */
    public static void startStopAllJobs(Scheduler scheduler, boolean start) {
        try {
            if (start) {
                scheduler.start();
            } else {
                if (!scheduler.isShutdown()) {
                    scheduler.shutdown();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建任务
     * @param scheduler
     * @param name
     * @param group
     * @param trigger
     * @param jobBean
     */
    private static void createJob(Scheduler scheduler, String name, String group, Trigger trigger, Class jobBean) {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobBean).withIdentity(name, group).build();
        try {
            //将触发器与任务绑定到调度器内
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static void createJob(Scheduler scheduler, String name, String group, Trigger trigger, Class jobBean,Map<String ,Object> param) {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobBean).withIdentity(name, group).build();
        try {
            if(param!=null && !param.isEmpty()){
                jobDetail.getJobDataMap().putAll(param);
            }
            //将触发器与任务绑定到调度器内
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



    /**
     * 移除任务
     * @param scheduler
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public static void removeJob(Scheduler scheduler, String name, String group) throws SchedulerException {
        Trigger trigger = ckJobIsExist(scheduler, name, group);
        if (trigger != null) {
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            JobKey jobKey = trigger.getJobKey();
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
        }
    }

    /**
     * 判断任务是否存在
     * @param scheduler
     * @param name
     * @param group
     * @return
     */
    public static Trigger ckJobIsExist(Scheduler scheduler, String name, String group) {
        Trigger trigger = null;
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        try {
            trigger = scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return trigger;
    }

    /**
     * 判断任务是否存在
     * @param scheduler
     * @return
     */
    public static JobDetail ckJobDetailIsExist(Scheduler scheduler,Trigger trigger) {
        JobDetail jobDetail=null;
        if(trigger!=null){
            JobKey jobKey= trigger.getJobKey();
            if(jobKey!=null){
                try {
                    jobDetail= scheduler.getJobDetail(jobKey);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
        return  jobDetail;
    }


}
