package com.example.rostrzep.googlebooksapp.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 *
 * Dagger custom qualifier for Rx-Java Scheduler working in the main thread
 */
@Qualifier
@Target({PARAMETER, FIELD, CONSTRUCTOR, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkScheduler {
}
