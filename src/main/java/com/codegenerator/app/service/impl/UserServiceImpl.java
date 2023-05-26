package com.codegenerator.app.service.impl;

import com.codegenerator.app.annotation.LogRecord;
import com.codegenerator.app.module.User;
import com.codegenerator.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @LogRecord(success = "更新了用户，从{queryUser{#user.id}} 更新为......", bizNo = "{{#user.id}}")
    @Override
    public void update(User user) {
        log.info("update @@@@@@@@@@@@@@@@@@@@@@@@");
        log.info("update @@@@@              @@@@@");
        log.info("update @@@@@@@@@@@@@@@@@@@@@@@@");

    }
}
