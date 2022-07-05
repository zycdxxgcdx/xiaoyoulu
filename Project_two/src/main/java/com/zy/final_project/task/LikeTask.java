package com.zy.final_project.task;

import com.zy.final_project.service.DynamicLikesService;
import com.zy.final_project.service.PublicPostService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class LikeTask extends QuartzJobBean  {
  @Autowired
  private DynamicLikesService dynamicLikesService;
  @Autowired
  private PublicPostService publicPostService;
  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    //将 Redis 里的点赞信息同步到数据库里
    dynamicLikesService.saveDynamicLikes();
    dynamicLikesService.saveDynamicLikesNums();
    publicPostService.addPostViewInMysql();
  }
}
