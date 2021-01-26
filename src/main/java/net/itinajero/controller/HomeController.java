package net.itinajero.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriaService;
import net.itinajero.service.IUsuarioService;
import net.itinajero.service.IVacanteService;

/**
 * @author JRonald
 *
 */
@Controller
public class HomeController {

	@Autowired
	private ICategoriaService serviceCategoria;

	@Autowired
	private IVacanteService serviceVacante;

	@Autowired
	private IUsuarioService serviceUsuario;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String mostrarHome(Model model) {
		// model.addAttribute("mensaje","Bienvenidos a Empleos App");
		// model.addAttribute("fecha",new Date());
		/*
		 * Contenido no dinámico String nombre = "Auxiliar de Contabilidad"; Date
		 * fechaPub = new Date(); double salario = 9000.0; boolean vigente = true;
		 * 
		 * model.addAttribute("nombre", nombre); model.addAttribute("fecha", fechaPub);
		 * model.addAttribute("salario", salario); model.addAttribute("vigente",
		 * vigente);
		 */

		// Contenido dinámico por servicio
		// List<Vacante> lista = serviceVacante.buscarTodas();
		// List<Vacante> lista = serviceVacante.buscarDestacadas();
		// model.addAttribute("vacantes",lista);

		return "home";
	}

	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		String username = auth.getName();
		System.out.println("Usuario: " + username);

		for (GrantedAuthority rol : auth.getAuthorities()) {
			System.out.println("ROL: " + rol.getAuthority());
		}

		if (session.getAttribute("usuario") == null) {
			Usuario usuario = serviceUsuario.buscarPorUsername(username);
			usuario.setPassword(null);
			System.out.println("Usuario en la sessión: " + usuario.toString());
			session.setAttribute("usuario", usuario);
		}

		return "redirect:/";
	}

	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("Buscando por:" + vacante);

		// where descripcion like '%?%'
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion",
				ExampleMatcher.GenericPropertyMatchers.contains());

		Example<Vacante> example = Example.of(vacante, matcher);

		List<Vacante> lista = serviceVacante.buscarByExample(example);
		model.addAttribute("vacantes", lista);

		return "home";
	}

	/*
	 * InitBinder para Strings si los detecta vacios en el Data Binding los settea a
	 * NULL
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("vacantes", serviceVacante.buscarDestacadas());
		model.addAttribute("categorias", serviceCategoria.buscarTodas());
		model.addAttribute("search", vacanteSearch);
	}

	@GetMapping("/listado")
	public String mostrarListado(Model model) {
		List<String> lista = new LinkedList<String>();
		lista.add("Ingeniero de Sistemas");
		lista.add("Auxiliar de contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");

		model.addAttribute("empleos", lista);

		return "listado";
	}

	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		Vacante vacante = new Vacante();
		vacante.setNombre("Ingeniero de comunicaciones");
		vacante.setDescripcion("Se solicita ingeniero para dar soporte a intranet");
		vacante.setFecha(new Date());
		vacante.setSalario(15000.0);

		model.addAttribute("vacante", vacante);

		return "detalle";
	}

	/*
	 * Esto usa el metodo getVacantes
	 * 
	 * @GetMapping("/tabla") public String mostrarTabla(Model model) { List<Vacante>
	 * lista = getVacantes(); model.addAttribute("vacantes",lista);
	 * 
	 * return "tabla"; }
	 */

	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = serviceVacante.buscarTodas();
		model.addAttribute("vacantes", lista);

		return "tabla";
	}

	/*
	 * Método que regresa una lista de objetos de tipo Vacante
	 */
	/*
	 * private List<Vacante> getVacantes() { List<Vacante> lista = new
	 * LinkedList<Vacante>(); SimpleDateFormat sdf = new
	 * SimpleDateFormat("dd-MM-yyyy");
	 * 
	 * try {
	 * 
	 * Vacante vacante1 = new Vacante(); vacante1.setId(1);
	 * vacante1.setNombre("Ingeniero de comunicaciones");
	 * vacante1.setDescripcion("Se solicita Ing. Civil para diseñar puente peatonal"
	 * ); vacante1.setFecha(sdf.parse("08-02-2019")); vacante1.setSalario(1000.0);
	 * vacante1.setDestacado(1); vacante1.setImagen("empresa1.png");
	 * 
	 * Vacante vacante2 = new Vacante(); vacante2.setId(2);
	 * vacante2.setNombre("Ingeniero de comunicaciones");
	 * vacante2.setDescripcion("Empresa requiere contador con 5 años de experiencia"
	 * ); vacante2.setFecha(sdf.parse("09-02-2019")); vacante2.setSalario(2000.0);
	 * vacante2.setDestacado(0); vacante2.setImagen("empresa2.png");
	 * 
	 * Vacante vacante3 = new Vacante(); vacante3.setId(3);
	 * vacante3.setNombre("Ingeniero de comunicaciones"); vacante3.
	 * setDescripcion("Empresa internacional requiere ingeniero mecánico para mantenimiento"
	 * ); vacante3.setFecha(sdf.parse("10-02-2019")); vacante3.setSalario(5000.0);
	 * vacante3.setDestacado(0); vacante3.setImagen("empresa3.png");
	 * 
	 * Vacante vacante4 = new Vacante(); vacante4.setId(4);
	 * vacante4.setNombre("Ingeniero de comunicaciones");
	 * vacante4.setDescripcion("Universidad privada requiere profesor con maestría"
	 * ); vacante4.setFecha(sdf.parse("11-02-2019")); vacante4.setSalario(18000.0);
	 * vacante4.setDestacado(1);
	 * 
	 * lista.add(vacante1); lista.add(vacante2); lista.add(vacante3);
	 * lista.add(vacante4);
	 * 
	 * } catch (ParseException e) { // TODO: handle exception
	 * System.out.println("Error: "+e.getMessage()); }
	 * 
	 * return lista; }
	 */

	@GetMapping("/signup")
	public String registrarse(Usuario usuario, Model model) {
		// model.addAttribute("perfiles", servicePerfil.buscarTodas());
		return "usuarios/formRegistro";
	}

	@GetMapping("/login")
	public String mostrarLogin() {
		// model.addAttribute("perfiles", servicePerfil.buscarTodas());
		return "formLogin";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	@PostMapping("/signup")
	public String guardaRegistro(Usuario usuario, BindingResult result, RedirectAttributes attributes, Model model) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
				attributes.addFlashAttribute("msg", "Verifique la información errónea.");
			}
			return "usuarios/formRegistro";
		}

		// Perfil tempPerfil = new Perfil(3, "USUARIO");
		Perfil tempPerfil = new Perfil();
		tempPerfil.setId(3);
		// encriptado
		String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		usuario.setPassword(pwdEncriptado);

		usuario.agregar(tempPerfil);
		usuario.setEstatus(1);
		usuario.setFechaRegistro(new Date());
		attributes.addFlashAttribute("msg", "Usuario agregado correctamente.");
		serviceUsuario.guardar(usuario);
		return "redirect:/usuarios/index";
	}

	@GetMapping("/bcrypt/{texto}")
	@ResponseBody
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " encriptado por Bcrypt: " + passwordEncoder.encode(texto);
	}

}
