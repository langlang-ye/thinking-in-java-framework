package com.langlang.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.Priority;

@Repository
@Primary
@Priority(3)
@Qualifier
public class MysqlDB implements DB {


}
