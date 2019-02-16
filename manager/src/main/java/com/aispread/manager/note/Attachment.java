package com.aispread.manager.note;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**附件表
 * @auther SyntacticSugar
 * @data 2019/2/15 0015上午 9:38
 */
@Entity
@Table(name="t_attachment_list")
public @Data class Attachment {

	@Id
	@Column(name="attachment_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long attachmentId; //附件id
	
	@Column(name="user_id")
	private String userId;    //用户id 在没有连接外键只是用来
								//查询用户表的
	
	@Column(name="attachment_name")
	private String attachmentName;  //附件名字
	
	@Column(name="attachment_path")
	private String attachmentPath;  //附件存储路径
	
	
	@Column(name="attachment_size")
	private Long attachmentSize; //附件大小
	
	@Column(name="attachment_type")
	private String attachmentType;  //附件类型
	
	@Column(name="upload_time")
	private Date uploadTime;     //附件上传时间
	
	private String model;          //所属模块
	
	@Column(name="attachment_shuffix")
	private String attachmentShuffix; //附件后缀

}
