package tz.go.tcra.lims.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.utils.exception.AuthException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Value("${spring.ldap.urls}")
    private String ldapUrls;

    @Value("${spring.ldap.base.dn}")
    private String ldapBaseDn;
    
    @Autowired
    private JwtConfig jwtFilter;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    //authentication configurer
    @Override
    protected void configure(AuthenticationManagerBuilder auth){   
        try{
            
            auth.userDetailsService(userDetailsService);
            auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
            
        }catch(Exception e){
        
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            throw new AuthException(e.getLocalizedMessage());
        }
        
    }
    
    //authorization configurer
    @Override
    protected void configure(HttpSecurity http){                
        try{
            
            http
            .cors()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(
                    "/lims-api/v1/authenticate",
                    "/v1/authenticate",
                    "/lims-api/v1/authenticate2",
                    "/v1/authenticate2",
                    "/lims-api/v1/user-registration/signup",
                    "/v1/user-registration/signup",
                    "/v1/user-registration/activation",
                    "/lims-api/v1/user-registration/activation",
                    "/v1/user-registration/forgot-password",
                    "/v1/user-registration/reset-password",
                    "/lims-api/v1/user-registration/forgot-password",
                    "/lims-api/v1/user-registration/reset-password",
                    "/lims-api/v1/request-control-number/controlnumber",
                    "/v1/request-control-number/controlnumber",
                    "/lims-api/v1/request-control-number/paid",
                    "/v1/request-control-number/paid",
                    "/lims-api/v2/api-docs", 
                    "/v2/api-docs", 
                    "/favicon.ico", 
                    "/configuration/ui", 
                    "/swagger-resources/**", 
                    "/configuration/security",
                    "/swagger-ui/**", 
                    "/swagger-ui.html", 
                    "/webjars/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            
        }catch(Exception e){
        
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            throw new AuthException(e.getLocalizedMessage());
        }        
    }
    
    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider = new ActiveDirectoryLdapAuthenticationProvider(ldapBaseDn, ldapUrls);
        activeDirectoryLdapAuthenticationProvider.setConvertSubErrorCodesToExceptions(true);
        activeDirectoryLdapAuthenticationProvider.setUseAuthenticationRequestCredentials(true);
        
        return activeDirectoryLdapAuthenticationProvider;
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        return encoder;
    }
    
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers=new ArrayList();
        providers.add(authenticationProvider());
        providers.add(activeDirectoryLdapAuthenticationProvider());

		return new ProviderManager(providers);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Set-Cookie", "Access-Control-Allow-Credentials","Access-Control-Allow-Methods", "Access-Control-Allow-Origin","Cache-Control"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
