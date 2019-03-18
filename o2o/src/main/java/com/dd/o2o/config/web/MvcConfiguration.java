package com.dd.o2o.config.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.dd.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.dd.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import com.google.code.kaptcha.servlet.KaptchaServlet;
@Configuration
@EnableWebMvc //等价于<mvc:annotation-driven/>
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{
	//Spring容器
	private ApplicationContext applicationContext;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}
	
	//静态资源配置
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		//registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
		registry.addResourceHandler("/upload/**").addResourceLocations("file:D:\\projectdev\\image\\upload\\");
	}
	
	//定义默认请求处理器
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// TODO Auto-generated method stub
		configurer.enable();
	}
	
	//创建视图解析器
	@Bean(name="viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
		viewResolver.setApplicationContext(applicationContext);
		viewResolver.setCache(false);
		viewResolver.setPrefix("/WEB-INF/html/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
	
	//文件上传解析器
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		//1024*1024*20=20M
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
	}
	
	
	@Value("${kaptcha.border}")
	private String border;
	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;
	@Value("${kaptcha.image.width}")
	private String width;
	@Value("${kaptcha.textproducer.char.string}")
	private String cString;
	@Value("${kaptcha.image.height}")
	private String height;
	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;
	@Value("${kaptcha.noise.color}")
	private String nColor;
	@Value("${kaptcha.textproducer.char.length}")
	private String clength;
	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;
	//验证码
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servlet=new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
		servlet.addInitParameter("kaptcha.border", border);
		servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor );
		servlet.addInitParameter("kaptcha.image.width", width);
		servlet.addInitParameter("kaptcha.textproducer.char.string",cString );
		servlet.addInitParameter("kaptcha.image.height", height);
		servlet.addInitParameter("kaptcha.textproducer.font.size",fsize );
		servlet.addInitParameter("kaptcha.noise.color", nColor);
		servlet.addInitParameter("kaptcha.textproducer.char.length",clength );
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);
		return servlet;
	}

	
	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String interceptPath="/shopadmin/**";
		//注册拦截器
		InterceptorRegistration loginIR=registry.addInterceptor(new ShopLoginInterceptor());
		loginIR.addPathPatterns(interceptPath);
		
		/*InterceptorRegistration permissionIR=registry.addInterceptor(new ShopPermissionInterceptor());
		permissionIR.addPathPatterns(interceptPath);
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
		permissionIR.excludePathPatterns("/shopadmin/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");*/
	}
	
	
	
}
