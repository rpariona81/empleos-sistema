package net.itinajero.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriaService;
import net.itinajero.service.IVacanteService;
import net.itinajero.util.Utileria;

@Controller
@RequestMapping("/vacantes")
public class VacantesController {

	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;

	@Autowired
	private IVacanteService serviceVacante;

	@Autowired
	private ICategoriaService serviceCategoria;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> lista = serviceVacante.buscarTodas();
		model.addAttribute("vacantes", lista);
		return "vacantes/listVacantes";
	}
	
	/*
	 * Paginacion en pantalla home
	 */
	@GetMapping(value = "/indexPaginate")
	public String mostraIndexPaginado(Model model, Pageable page) {
		Page<Vacante> lista=serviceVacante.buscarTodas(page);
		model.addAttribute("vacantes",lista);
		return "vacantes/listVacantes";
	}

	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) {
		//model.addAttribute("categorias", serviceCategoria.buscarTodas());
		return "vacantes/formVacante";
	}

	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}

		if (!multiPart.isEmpty()) {
			// String ruta = "/empleos/img-vacantes/"; // Linux/MAC
			// String ruta = "D:/WorkspaceSTS/empleos/img-vacantes/"; // Windows
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) { // La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen);
			}
		}

		serviceVacante.guardar(vacante);
		attributes.addFlashAttribute("msg", "Registro guardado");
		System.out.println("Vacante: " + vacante);

		// Renderiza la tabla
		// return "vacantes/listVacantes";

		// Renderiza la tabla con petición GET
		//return "redirect:/vacantes/index";
		return "redirect:/vacantes/indexPaginate";
	}

	// Sin Data Binding
	/*
	 * @PostMapping("/save") public String guardar(@RequestParam("nombre") String
	 * nombre, @RequestParam("descripcion") String descripcion,
	 * 
	 * @RequestParam("estatus") String estatus, @RequestParam("fecha") String fecha,
	 * 
	 * @RequestParam("destacado") Integer destacado, @RequestParam("salario") Double
	 * salario,
	 * 
	 * @RequestParam("detalles") String detalles) { System.out.println("nombre: " +
	 * nombre); System.out.println("descripcion: " + descripcion);
	 * System.out.println("estatus: " + estatus); System.out.println("fecha: " +
	 * fecha); System.out.println("destacado: " + destacado);
	 * System.out.println("salario: " + salario); System.out.println("detalles: " +
	 * detalles); return "vacantes/listVacantes"; }
	 */

	/*
	 * @GetMapping("/delete") public String eliminar(@RequestParam("id") int
	 * idVacante, Model model) { System.out.println("Borrando vacante con id: " +
	 * idVacante); model.addAttribute("id", idVacante); return "mensaje"; }
	 */

	// Implementacion en base de datos
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante, RedirectAttributes attributes, Model model) {
		System.out.println("Borrando vacante con id: " + idVacante);
		serviceVacante.eliminar(idVacante);
		attributes.addFlashAttribute("msg", "La vacante fue eliminada!");
		// Renderiza la tabla con petición GET
		//return "redirect:/vacantes/index";
		return "redirect:/vacantes/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante=serviceVacante.buscarPorId(idVacante);
		model.addAttribute("vacante",vacante);
		//model.addAttribute("categorias", serviceCategoria.buscarTodas());
		// Renderiza la tabla con petición GET
		return "vacantes/formVacante";
	}
	
	//Con esta mejora borramos esta linea en los otros metodos
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", serviceCategoria.buscarTodas());
	}

	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante, Model model) {
		/*
		 * System.out.println("idVacante " + idVacante); // Buscar en la base de
		 * datos... model.addAttribute("idVacante", idVacante); return
		 * "vacantes/detalleId";
		 */

		// Ahora usamos el servicio
		Vacante vacante = serviceVacante.buscarPorId(idVacante);
		System.out.println("Vacante " + vacante);
		model.addAttribute("vacante", vacante);

		return "detalle";
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}
