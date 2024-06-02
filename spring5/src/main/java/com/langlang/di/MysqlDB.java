package com.langlang.di;

import org.springframework.context.annotation.Primary;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import javax.annotation.Priority;

@Repository
// @Primary
@Priority(3)
public class MysqlDB implements DB {


}
