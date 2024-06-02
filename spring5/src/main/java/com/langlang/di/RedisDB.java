package com.langlang.di;

import org.springframework.stereotype.Repository;

import javax.annotation.Priority;

@Repository
@Priority(3)
public class RedisDB implements DB{
}
