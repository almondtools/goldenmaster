package net.amygdalum.goldenmaster;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GoldenMasterExtension.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoldenMasterTest {

	String store() default "";
	boolean interactive() default true;
}
