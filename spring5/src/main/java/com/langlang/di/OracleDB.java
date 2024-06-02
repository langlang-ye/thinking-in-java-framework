package com.langlang.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import javax.annotation.Priority;

@Repository
@Priority(1)
public class OracleDB implements DB {
}
