package cn.stackflow.aums.common.shiro;

import cn.stackflow.aums.web.ApiVersion;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-18 10:24
 */
@Configuration
public class ShiroConfiguration {


    //不加这个注解不生效，具体不详
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }

    /*
     * a. 告诉shiro不要使用默认的DefaultSubject创建对象，因为不能创建Session
     * */
    @Bean
    public SubjectFactory jwtDefaultSubjectFactory() {
        return new DefaultWebSubjectFactory() {
            public Subject createSubject(SubjectContext context) {
                //不创建session
                context.setSessionCreationEnabled(false);
                return super.createSubject(context);
            }
        };
    }

//    @Bean
//    public Realm jwtRealm() {
//        return new JwtRealm();
//    }

    @Bean
    public Realm usernamePasswordRealm() {
        return new UserRealm();
    }



    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setAuthenticator(modularRealmAuthenticator());
//        securityManager.setRealms(Arrays.asList(usernamePasswordRealm(),jwtRealm()));
        securityManager.setRealms(Arrays.asList(usernamePasswordRealm()));
        // 关闭 ShiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //禁止Subject的getSession方法
        securityManager.setSubjectFactory(jwtDefaultSubjectFactory());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //使用自定义认证拦截器
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> map = new LinkedHashMap<>();
        map.put(ApiVersion.VERSION + "/login", "anon");
        map.put(ApiVersion.VERSION + "/logout", "anon");
        map.put(ApiVersion.VERSION + "/common/**", "anon");
//        map.put(ApiVersion.VERSION + "/**", "anonc");
        map.put(ApiVersion.VERSION + "/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
//
//    @Bean
//    public ModularRealmAuthenticator modularRealmAuthenticator() {
//        //自己重写的ModularRealmAuthenticator
//        NavigationModularRealmAuthenticator modularRealmAuthenticator = new NavigationModularRealmAuthenticator();
//        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//        return modularRealmAuthenticator;
//    }


}
