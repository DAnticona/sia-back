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
import pe.com.aldesa.aduanero.service.TarjetaService;

@RestController
@RequestMapping("/v1")
public class TarjetaRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private TarjetaService tarjetaService;

	@Autowired
	public TarjetaRestController(TarjetaService tarjetaService) {
		this.tarjetaService = tarjetaService;
	}

	@GetMapping("/tarjetas")
	public ResponseEntity<?> findAll() {
		ApiResponse response = tarjetaService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/tarjetas/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		ApiResponse response = tarjetaService.findById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/tarjetas")
	public ResponseEntity<?> create(@RequestBody String request) {
		ApiResponse response;
		try {
			response = tarjetaService.save(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/tarjetas")
	public ResponseEntity<?> update(@RequestBody String request) {
		ApiResponse response;
		try {
			response = tarjetaService.update(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/tarjetas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		ApiResponse response;
		try {
			response = tarjetaService.delete(id);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(response);
	}

}
