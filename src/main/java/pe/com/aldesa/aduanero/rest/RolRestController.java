package pe.com.aldesa.aduanero.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.service.RolService;

@RestController
@RequestMapping("/v1")
public class RolRestController {
	
	private RolService rolService;
	
	@Autowired
	public RolRestController(RolService rolService) {
		this.rolService = rolService;
	}
	
	@GetMapping("/rol")
	public List<Rol> findAll() {
		return rolService.findAll();
	}
	
	@GetMapping("/rol/{id}")
	public Rol findById(@PathVariable Integer id) {
		return rolService.findById(id);
	}
	
	@PostMapping("/rol")
	public Rol create(@RequestBody Rol rol) {
		return rolService.save(rol);
	}
	
	@PutMapping("/rol")
	public Rol update(@RequestBody Rol rol) {
		return rolService.save(rol);
	}
	
	@DeleteMapping("/rol/{id}")
	public String delete(@PathVariable Integer id) {
		rolService.delete(id);
		return "Rol " + id + " eliminado";
	}

}
