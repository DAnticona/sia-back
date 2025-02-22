package pe.com.aldesa.aduanero.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.service.MenuService;

@RestController
@RequestMapping("/v1")
public class MenuRestController {

	private MenuService menuService;

	@Autowired
	public MenuRestController(MenuService menuService) {
		this.menuService = menuService;
	}

	@GetMapping("/menus")
	public ResponseEntity<?> findAll() {
		ApiResponse response = menuService.findAll();
		return ResponseEntity.ok(response);
	}

}
