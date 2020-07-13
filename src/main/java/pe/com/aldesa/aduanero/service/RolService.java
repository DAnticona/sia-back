package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.repository.RolRepository;

@Service
public class RolService {
	
	private RolRepository rolRepository;
	
	@Autowired
	public RolService(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}
	
	public List<Rol> findAll() {
		return rolRepository.findAll();
	}
	
	public Rol save(Rol rol) {
		rol.setNombre(rol.getNombre().toUpperCase());
		return rolRepository.save(Preconditions.checkNotNull(rol));
	}
	
	public void delete(Integer id) {
		rolRepository.deleteById(Preconditions.checkNotNull(id));
	}
	
	public Rol findById(Integer id) {
		return rolRepository.findById(Preconditions.checkNotNull(id)).orElse(null);
	}
	
}
