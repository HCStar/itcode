package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PersonService personService;

}
