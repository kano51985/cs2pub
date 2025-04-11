package com.sana.mapper.delay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sana.domain.entity.delay.DelayedTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DelayedTaskMapper extends BaseMapper<com.sana.domain.entity.delay.DelayedTask> {
    /**
     * 更新任务状态
     */
    @Update("UPDATE delayed_tasks SET status = #{status}, update_time = NOW() WHERE id = #{taskId}")
    int updateStatus(@Param("taskId") String taskId, @Param("status") DelayedTask.TaskStatus status);

    /**
     * 查询待处理任务
     */
    List<DelayedTask> selectPendingTasks(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
