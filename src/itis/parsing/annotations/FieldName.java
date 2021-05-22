package itis.parsing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//значение в поле с такой аннотацией нужно брать из другого поля. Имя "поля-источника значения" передается в параметр аннотации.
//Например если @FieldName("target"), то в файле  нужно искать поле "target", а не поле, над которым стоит аннотация.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldName {

    String value();
}
