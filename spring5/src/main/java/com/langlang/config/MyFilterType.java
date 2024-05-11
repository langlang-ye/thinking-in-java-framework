package com.langlang.config;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.SocketUtils;

public class MyFilterType implements TypeFilter {

	/*
	MetadataReader: metadataReader the metadata reader for the target class
	metadataReader: 读取到的当前正在扫描的类的信息
	MetadataReaderFactory: metadataReaderFactory a factory for obtaining metadata readers
	 for other classes (such as superclasses and interfaces)
	 metadataReaderFactory:可以获取到其他任何类信息的
	 * 
	 */
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {

		//获取当前类注解的信息
		AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
		
		//获取当前正在扫描的类的类信息
		ClassMetadata classMetadata = metadataReader.getClassMetadata();
		
		//获取当前类资源（类的路径）
		Resource resource = metadataReader.getResource();
		
		String className = classMetadata.getClassName();
		System.out.println("--->" + className);
		
		if (className.contains("er")) {
			System.out.println("满足条件: " + className);
			return true;
		}
		
		return false;
	}

}
