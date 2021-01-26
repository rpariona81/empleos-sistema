package net.itinajero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Value("${empleosapp.ruta.imagenes}")
	private String rutaImagenes;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //specifying static resource location for themes related files(css etc)
        //registry.addResourceHandler("/logos/**").addResourceLocations("file:/empleos/img-vacantes/"); //Linux
        //registry.addResourceHandler("/logos/**").addResourceLocations("file:c:/empleos/img-vacantes/"); //Windows
		////Para mi ruta en Windows
        //registry.addResourceHandler("/logos/**").addResourceLocations("file:d:/WorkspaceSTS/empleos/img-vacantes/");
        //Codigo mejorado
        registry.addResourceHandler("/logos/**").addResourceLocations("file:"+rutaImagenes);
    }

}
