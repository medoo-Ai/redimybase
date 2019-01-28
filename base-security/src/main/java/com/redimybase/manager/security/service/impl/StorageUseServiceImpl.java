package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.StorageUseEntiry;
import com.redimybase.manager.security.mapper.StorageUseMapper;
import com.redimybase.manager.security.service.StorageUseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入库与领用 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Service
public class StorageUseServiceImpl extends ServiceImpl<StorageUseMapper, StorageUseEntiry> implements
    StorageUseService {

}
