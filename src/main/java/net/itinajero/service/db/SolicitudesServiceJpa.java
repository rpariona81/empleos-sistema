package net.itinajero.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Solicitud;
import net.itinajero.repository.SolicitudesRepository;
import net.itinajero.service.ISolicitudesService;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService {

	@Autowired
	private SolicitudesRepository repoSolicitud;

	@Override
	public void guardar(Solicitud solicitud) {
		repoSolicitud.save(solicitud);
	}

	@Override
	public void eliminar(Integer idSolicitud) {
		repoSolicitud.deleteById(idSolicitud);
	}

	@Override
	public List<Solicitud> buscarTodas() {
		return repoSolicitud.findAll();
	}

	@Override
	public Solicitud buscarPorId(Integer idSolicitud) {
		Optional<Solicitud> solicitud = repoSolicitud.findById(idSolicitud);
		if (solicitud.isPresent()) {
			return solicitud.get();
		}
		return null;
	}

	@Override
	public Page<Solicitud> buscarTodas(Pageable page) {
		return repoSolicitud.findAll(page);
	}

}
