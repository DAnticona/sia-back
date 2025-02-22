package pe.com.aldesa.aduanero.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.dto.ErrorResponse;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.service.MovimientoService;

@RestController
@RequestMapping("/v1")
public class MovimientoRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private MovimientoService movimientoService;

	@Autowired
	public MovimientoRestController(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}

	@GetMapping("/movimientos")
	public ResponseEntity<?> findAll() {
		ApiResponse response = movimientoService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/movimientos/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		ApiResponse response = movimientoService.findById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/movimientos")
	public ResponseEntity<?> create(@RequestBody String request) {
		ApiResponse response;
		try {
			response = movimientoService.save(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/movimientos")
	public ResponseEntity<?> update(@RequestBody String request) {
		ApiResponse response;
		try {
			response = movimientoService.update(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

}
