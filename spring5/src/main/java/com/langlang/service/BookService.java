package com.langlang.service;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.langlang.dao.BookDao;

@Service
public class BookService {

	@Qualifier("bookDao")
//	@Autowired(required = false)
//	@Resource(name="bookDao2")
	@Inject  // TODO @Resource @Inject 与  @Autowired 区别， 对比不同版本的实现
	private BookDao bookDao;
	
	public void print() {
		System.out.println(bookDao);
	}

	@Override
	public String toString() {
		return "BookService [bookDao=" + bookDao + "]";
	}
	
	
}
