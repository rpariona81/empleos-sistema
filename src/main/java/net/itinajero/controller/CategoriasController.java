package net.itinajero.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Categoria;
import net.itinajero.service.ICategoriaService;

@Controller
@RequestMapping(value = "/categorias")
public class CategoriasController {

	@Autowired
	private ICategoriaService serviceCategoria;

	// @GetMapping("/index")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String mostrarIndex(Model model) {
		List<Categoria> categorias = serviceCategoria.buscarTodas();
		model.addAttribute("categorias", categorias);
		return "categorias/listCategorias";
	}
	
	/*
	 * Paginacion en pantalla home
	 */
	@GetMapping(value = "/indexPaginate")
	public String mostraIndexPaginado(Model model, Pageable page) {
		Page<Categoria> lista=serviceCategoria.buscarTodas(page);
		model.addAttribute("categorias",lista);
		return "categorias/listCategorias";
	}

	// @GetMapping("/create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String crear(Categoria categoria, Model model) {
		return "categorias/formCategoria";
	}

	@PostMapping("/save")
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "categorias/formCategoria";
		}
		serviceCategoria.guardar(categoria);
		attributes.addFlashAttribute("msg", "Registro guardado");
		System.out.println("Categoria: " + categoria);
		//return "redirect:/categorias/index";
		return "redirect:/categorias/indexPaginate";
	}

	/*
	 * // @PostMapping("/save")
	 * 
	 * @RequestMapping(value = "/save", method = RequestMethod.POST) public String
	 * guardar(@RequestParam("nombre") String nombre,@RequestParam("descripcion")
	 * String descripcion) { System.out.println("Categoria "+nombre);
	 * System.out.println("Descripcion "+descripcion); return
	 * "categorias/listCategorias"; }
	 */

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Integer idCategoria, RedirectAttributes attributes, Model model) {
		serviceCategoria.eliminar(idCategoria);
		attributes.addFlashAttribute("msg", "Registro eliminado");
		System.out.println("idCategoria eliminado: " + idCategoria);
		//return "redirect:/categorias/index";
		return "redirect:/categorias/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Categoria categoria=serviceCategoria.buscarPorId(idVacante);
		model.addAttribute("categoria",categoria);
		//model.addAttribute("categorias", serviceCategoria.buscarTodas());
		// Renderiza la tabla con petición GET
		return "categorias/formCategoria";
	}
	
}
