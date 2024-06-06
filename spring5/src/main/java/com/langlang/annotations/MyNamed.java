package com.langlang.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * javax.inject.Qualifier, @Target(ANNOTATION_TYPE) 只能标注在注解上面.
 *
 */
@Qualifier
public @interface MyNamed {


}
