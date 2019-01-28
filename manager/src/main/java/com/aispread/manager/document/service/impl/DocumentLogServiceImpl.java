package com.aispread.manager.document.service.impl;

import com.aispread.manager.document.entity.DocumentLogEntity;
import com.aispread.manager.document.mapper.DocumentLogMapper;
import com.aispread.manager.document.service.DocumentLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文档库操作日志表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Service
public class DocumentLogServiceImpl extends ServiceImpl<DocumentLogMapper, DocumentLogEntity> implements DocumentLogService {

}
