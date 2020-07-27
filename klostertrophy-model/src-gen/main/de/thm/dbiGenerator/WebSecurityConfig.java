package de.thm.dbiGenerator;

import de.thm.dbiGenerator.repositories.AdminRepository;
import de.thm.dbiGenerator.entities.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private AdminRepository adminRepository;
	
	@Autowired
	public WebSecurityConfig(AdminRepository adminRepository){
		this.adminRepository = adminRepository;
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF is handled by Vaadin: https://vaadin.com/framework/security
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and().logout().logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                // allow Vaadin URLs and the login URL without authentication
                .regexMatchers("/login.*", "/accessDenied", "/VAADIN/.*", "/favicon.ico", "/robots.txt", "/manifest.webmanifest",
                        "/sw.js", "/offline-page.html", "/frontend/.*", "/webjars/.*", "/frontend-es5/.*", "/frontend-es6/.*").permitAll()
                .regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
                // deny any other URL until authenticated
                .antMatchers("/**").fullyAuthenticated()
            /*
             Note that anonymous authentication is enabled by default, therefore;
             SecurityContextHolder.getContext().getAuthentication().isAuthenticated() always will return true.
             Look at LoginView.beforeEnter method.
             more info: https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/anonymous.html
             */
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        for(Admin admin:adminRepository.findAll()){
        	auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        		.withUser(admin.getName()).password(admin.getPassword()).roles("ADMIN");
        }
    }

    /**
     * Expose the AuthenticationManager (to be used in LoginView)
     *
     * @return
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
