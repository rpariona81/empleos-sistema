package net.itinajero.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;

	/*
	 * Con esto se tiene acceso a las tablas por defecto de spring security
	 * 
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception{ auth.jdbcAuthentication().dataSource(datasource); }
	 */

	/*
	 * Con esto se tiene acceso a las tablas personalizadas para que se conecten con
	 * Spring Security
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(datasource)
				.usersByUsernameQuery("SELECT username, password, estatus from Usuarios WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT u.username, p.perfil FROM UsuarioPerfil up "
						+ "INNER JOIN Usuarios u ON u.id = up.idUsuario "
						+ "INNER JOIN Perfiles p ON p.id = up.idPerfil " + "WHERE u.username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Los recursos estaticos no requieren autenticacion
				.antMatchers("/bootstrap/**", "/images/**", "/tinymce/**", "/logos/**").permitAll()
				// Las vistas publicas no requieren autenticacion
				.antMatchers("/","/signup", "/search", "/vacantes/view/**","/bcrypt/**").permitAll()
				// Asignar permisos a URLs por ROLES
				.antMatchers("/solicitudes/create/**").hasAnyAuthority("USUARIO")
				.antMatchers("/solicitudes/save/**").hasAnyAuthority("USUARIO")
				.antMatchers("/vacantes/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
				.antMatchers("/categorias/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
				.antMatchers("/solicitudes/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
				.antMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")
				// Todas las demás URLs requieren autenticacion
				.anyRequest().authenticated()
				// El formulario de Login no requiere autenticacion
				.and().formLogin().loginPage("/login").permitAll();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
	}

}
