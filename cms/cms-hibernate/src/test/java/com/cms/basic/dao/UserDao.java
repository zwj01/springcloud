package com.cms.basic.dao;

import com.cms.basic.model.User;
import com.cms.dao.impl.BaseDao;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {
}
