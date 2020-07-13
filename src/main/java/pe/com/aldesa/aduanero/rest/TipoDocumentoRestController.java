package pe.com.aldesa.aduanero.rest;

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
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.service.DefaultTipoDocumentoService;

@RestController
@RequestMapping("/v1")
public class TipoDocumentoRestController {
	
	private DefaultTipoDocumentoService tipoDocumentoService;
	
	@Autowired
	public TipoDocumentoRestController(DefaultTipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	
	@GetMapping("/tipoDocumento")
	public ResponseEntity<?> findAll() {
		ApiResponse response = null;
		try {
			response = tipoDocumentoService.findAll();
		} catch (ApiException e) {
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/tipoDocumento/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		ApiResponse response;
		try {
			response = tipoDocumentoService.findById(id);
		} catch (ApiException e) {
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/tipoDocumento")
	public ResponseEntity<?> create(@RequestBody TipoDocumento tipoDocumento) {
		ApiResponse response;
		try {
			response = tipoDocumentoService.save(tipoDocumento);
		} catch (ApiException e) {
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/tipoDocumento")
	public ResponseEntity<?> update(@RequestBody TipoDocumento tipoDocumento) {
		ApiResponse response;
		try {
			response = tipoDocumentoService.save(tipoDocumento);
		} catch (ApiException e) {
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/tipoDocumento/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		ApiResponse response;
		try {
			response = tipoDocumentoService.delete(id);
		} catch (ApiException e) {
			return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
