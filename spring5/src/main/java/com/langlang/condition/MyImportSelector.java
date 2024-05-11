package com.langlang.condition;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.MultiValueMap;

//自定义逻辑返回需要导入的组件
public class MyImportSelector implements ImportSelector{

	//返回值，就是到导入到容器中的组件全类名
	//AnnotationMetadata:当前标注@Import注解的类的所有注解信息
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {

		//AnnotationMetadata 实际类型是  StandardAnnotationMetadata 里面的 Annotation[] annotations 属性存
		//储了标注@Import注解的类(MainConfig2.class) 的三个注解 @Conditional @Configuration @Import
		
		System.out.println("--------------------------------------");
	/* 	StandardAnnotationMetadata annotationMetadata = (StandardAnnotationMetadata)importingClassMetadata;
		
		Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
		System.out.println(annotationTypes);
		System.out.println(annotationTypes.getClass());
		
		MultiValueMap<String, Object> allAnnotationAttributes = annotationMetadata.getAllAnnotationAttributes("org.springframework.context.annotation.Import");
		System.out.println(allAnnotationAttributes);
		System.out.println(annotationMetadata.getClass() + "{}" + annotationMetadata.getClassName());
		System.out.println(annotationMetadata.getAnnotatedMethods("org.springframework.context.annotation.Scope"));
		 */
		System.out.println("--------------------------------------");
		
		//importingClassMetadata
		//方法不要返回null值   可以是空的字符串数组  new String[]{}
		
		return new String[]{"com.langlang.bean.Blue", "com.langlang.bean.Yellow"};
	}

}
