package pe.com.aldesa.aduanero.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import pe.com.aldesa.aduanero.service.ServicioService;

@RestController
@RequestMapping("/v1")
public class ServicioRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ServicioService servicioService;

	@Autowired
	public ServicioRestController(ServicioService servicioService) {
		this.servicioService = servicioService;
	}

	@GetMapping("/servicios")
	public ResponseEntity<?> findAll() {
		ApiResponse response = servicioService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/servicios/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		ApiResponse response = servicioService.findById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/servicios")
	public ResponseEntity<?> create(@RequestBody String request) {
		ApiResponse response;
		try {
			response = servicioService.save(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/servicios")
	public ResponseEntity<?> update(@RequestBody String request) {
		ApiResponse response;
		try {
			response = servicioService.update(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/servicios/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		ApiResponse response;
		try {
			response = servicioService.delete(id);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(response);
	}

}
