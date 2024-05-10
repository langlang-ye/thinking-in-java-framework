package com.langlang.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
	
	//@EventListener(classes={ApplicationEvent.class}) // 可以写一个, 或者写一个数组
	@EventListener(classes = ApplicationEvent.class) // 可以写一个, 或者写一个数组
	public void listen(ApplicationEvent event) {
		System.out.println("PersonService.... 监听到的事件：" + event);
	}

}
