package org.psawesome.chapters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// tag::chap01[]
/*
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
*/

//@EnableAsync
// end::chap01[]
@SpringBootApplication
public class ChaptersApplication /*implements AsyncConfigurer*/ {

  public static void main(String[] args) {
    SpringApplication.run(ChaptersApplication.class, args);
  }
// tag::chap01[]
/*
@Override
public Executor getAsyncExecutor() {
  ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  executor.setCorePoolSize(2);
  executor.setMaxPoolSize(100);
  executor.setQueueCapacity(5);
  executor.initialize();
  return executor;
}

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
*/
// end::chap01[]

}
