package com.example.project.module.blog.dal.dataobject.communicationtype;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 群聊类型管理 DO
 *

 */
@TableName("blog_communication_type")
@KeySequence("blog_communication_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationTypeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 类型的名称
     */
    private String typeName;
    /**
     * 类型的描述
     */
    private String typeDes;
    /**
     * 类型的图片
     */
    private String typeImage;
    /**
     * 用来建立ws连接的类型（英文）
     */
    private String type;

}
