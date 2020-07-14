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
import pe.com.aldesa.aduanero.service.DefaultRolService;

@RestController
@RequestMapping("/v1")
public class RolRestController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DefaultRolService rolService;
	
	@Autowired
	public RolRestController(DefaultRolService rolService) {
		this.rolService = rolService;
	}
	
	@GetMapping("/rol")
	public ResponseEntity<?> findAll() {
		ApiResponse response = null;
		try {
			response = rolService.findAll();
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/rol/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		ApiResponse response;
		try {
			response = rolService.findById(id);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/rol")
	public ResponseEntity<?> create(@RequestBody String request) {
		ApiResponse response;
		try {
			response = rolService.save(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/rol")
	public ResponseEntity<?> update(@RequestBody String request) {
		ApiResponse response;
		try {
			response = rolService.update(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/rol/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		ApiResponse response;
		try {
			response = rolService.delete(id);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
