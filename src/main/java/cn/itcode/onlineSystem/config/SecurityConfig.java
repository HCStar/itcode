package cn.itcode.onlineSystem.config;

import cn.itcode.onlineSystem.constans.CommonConstant;
import cn.itcode.onlineSystem.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommonConstant {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        super.configure(web);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                "/webjars/**",
                "/static/**",
                "/login",
                "/register",
                "/sendCode",
                "/upload",
                "user/profile/*"
        ).hasAnyAuthority(
                AUTHORITY_USER,
                AUTHORITY_ADMIN
        )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**"
                ).hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll()
                .and().csrf().disable();

        //权限不够时的处理
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            //没有登录
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                String xRequestedWith = request.getHeader("x-requested-with");
                if("XMLHttpRequest".equals(xRequestedWith)){
                    response.setContentType("application/plain;charset=utf8");
                    PrintWriter writer = response.getWriter();
                    writer.write(HelperUtil.getJSONString(403, "你还没有登录哦!"));
                }else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            }
        })
         .accessDeniedHandler(new AccessDeniedHandler() {
             //权限不足
             @Override
             public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                 String xRequestedWith = request.getHeader("x-requested-with");
                 if("XMLHttpRequest".equals(xRequestedWith)){
                     response.setContentType("application/plain;charset=utf8");
                     PrintWriter writer = response.getWriter();
                     writer.write(HelperUtil.getJSONString(403, "你没有访问此功能的权限!"));
                 }else {
                     response.sendRedirect(request.getContextPath() + "/denied");
                 }
             }
         });

        //退出登录
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
}
