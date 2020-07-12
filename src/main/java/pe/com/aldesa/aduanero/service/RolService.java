package pe.com.aldesa.aduanero.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.repository.RolRepository;

@Service
public class RolService {
	
	private RolRepository rolDAO;
	
	@Autowired
	public RolService(RolRepository rolDAO) {
		this.rolDAO = rolDAO;
	}
	
	public List<Rol> findAll() {
		return rolDAO.findAll();
	}
	
	public Rol save(Rol rol) {
		return rolDAO.save(Preconditions.checkNotNull(rol));
	}
	
	public String delete(Integer id) throws ApiException {
		Optional<Rol> rolFound = rolDAO.findById(Preconditions.checkNotNull(id));
		if (rolFound.isPresent())
			rolDAO.deleteById(id);
		else
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		return "Rol con id - " + id + " eliminado";
	}
	
	public Rol findById(Integer id) throws ApiException {
		Optional<Rol> rolFound = rolDAO.findById(Preconditions.checkNotNull(id));
		if (rolFound.isPresent())
			return rolFound.get();
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
	
}
