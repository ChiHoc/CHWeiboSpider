package com.chiho.server.security

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 9:14 下午
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    @Bean
    fun filterRegistrationBean(): FilterRegistrationBean<*> {
        val bean = FilterRegistrationBean<OncePerRequestFilter>()
        bean.setFilter(AuthFilter())
        bean.addUrlPatterns("/*")
        return bean
    }
}