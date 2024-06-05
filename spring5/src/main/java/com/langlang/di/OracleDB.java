package com.langlang.di;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.Priority;

@Repository
@Primary
@Priority(1)
public class OracleDB implements DB {
}
