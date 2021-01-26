package net.itinajero.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.itinajero.model.Categoria;
import net.itinajero.model.Vacante;

@Service
//@Primary
public class CategoriaServiceImpl implements ICategoriaService{
	
	private List<Categoria> lista = null;
	
	public CategoriaServiceImpl() {
		
		lista=new LinkedList<Categoria>();
		
		Categoria cat1=new Categoria();
		cat1.setId(1);
		cat1.setNombre("Ventas");
		cat1.setDescripcion("Departamento de ventas y comercio");
		
		Categoria cat2=new Categoria();
		cat2.setId(2);
		cat2.setNombre("Contabilidad");
		cat2.setDescripcion("Contabilidad y Finanzas");
		
		Categoria cat3=new Categoria();
		cat3.setId(3);
		cat3.setNombre("Transporte");
		cat3.setDescripcion("Movilidad privada y pública");
		
		Categoria cat4=new Categoria();
		cat4.setId(4);
		cat4.setNombre("Informática");
		cat4.setDescripcion("Tecnologías de Información");
		
		Categoria cat5=new Categoria();
		cat5.setId(5);
		cat5.setNombre("Construcción");
		cat5.setDescripcion("Edificaciones urbanas");
		
		lista.add(cat1);
		lista.add(cat2);
		lista.add(cat3);
		lista.add(cat4);
		lista.add(cat5);
		
	}

	@Override
	public void guardar(Categoria categoria) {
		lista.add(categoria);
		
	}

	@Override
	public List<Categoria> buscarTodas() {
		return lista;
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		
		for (Categoria c : lista) {
			if (c.getId()==idCategoria) {
				return c;
			}
		}
		return null;
	}

	@Override
	public void eliminar(Integer idCategoria) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

}
