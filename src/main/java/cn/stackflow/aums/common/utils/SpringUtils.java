package cn.stackflow.aums.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author zhangchao
 * @Date 2019/6/14 10:33
 * @Version v1.0
 */
@Component
@Slf4j
public class SpringUtils implements ApplicationContextAware {


    private static ApplicationContext applicationContext;
    private static ConfigurableApplicationContext configurableContext ;
    private static BeanDefinitionRegistry beanDefinitionRegistry ;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("==== applicationContext =====");
        SpringUtils.applicationContext = applicationContext;
        if(SpringUtils.applicationContext != null){
            configurableContext = (ConfigurableApplicationContext) applicationContext;
            beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }
    public static <T> T getBean(Class c){
        return (T) applicationContext.getBean(c);
    }
}
