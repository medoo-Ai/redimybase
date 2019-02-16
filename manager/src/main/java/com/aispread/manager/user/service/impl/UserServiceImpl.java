package com.aispread.manager.user.service.impl;

import com.aispread.manager.user.entity.User;
import com.aispread.manager.user.mapper.UserMapper;
import com.aispread.manager.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SyntacticSugar
 * @since 2019-02-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
