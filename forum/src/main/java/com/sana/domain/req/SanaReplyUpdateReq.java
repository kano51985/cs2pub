package com.sana.domain.req;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 更新回复请求类
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanaReplyUpdateReq {
    @NotBlank(message = "id不能为空")
    private String id;
    private String topicId;
    private String content;
    private int floor;
    private int status;
    private String creator;
    private String updater;
}
