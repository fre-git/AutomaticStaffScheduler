package GroepswerkJava.DigitalPlanner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(authorize -> authorize
                        // access for everyone
                        .requestMatchers("/register/**", "/login/**", "/index", "/", "/css/**", "/static/**",
                                "/newUserPassword", "/editUserLogin/**").permitAll()
                        // access only for ADMIN role
                        .requestMatchers( "/staff", "/staffFunction/**", "/staffList", "/editStaff/**",
                                "/company/**", "/contractType", "/deleteContractType", "/editContractType/**",
                                "/department", "/deleteDepartment", "/editDepartment/**", "/users", "/weekPlanning/approve",
                                "/rangeWeek", "/RangeWeek", "/planningDetails/**", "/log/**", "/log-details", "/editAvailability"
                        , "/availabilities", "/newStaff", "/deleteStaffFunction", "/editStaffFunction/**" ).hasRole("ADMIN")
                        // access only if user has USER or ADMIN role (user has to be logged in)
                        .requestMatchers("/dashboard", "/workSchedule", "/assignShift", "/staff/staffDetail",
                                "/planning", "/assignShift", "/handleWorkingDays", "/staff/myProfile").hasAnyRole("ADMIN", "USER")

                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
