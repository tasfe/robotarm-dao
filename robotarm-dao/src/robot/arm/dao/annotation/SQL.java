package robot.arm.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SQL {
	/**
	 * 
	 * @return 增强SQL
	 */
	String value();
	
	/**
	 * 
	 * @return 操作类型
	 */
	SQLType type() default SQLType.WRITE;
}
