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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.dto.ErrorResponse;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.service.CotizacionDetalleService;

@RestController
@RequestMapping("/v1")
public class CotizacionDetalleRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private CotizacionDetalleService cotizacionDetalleService;

	@Autowired
	public CotizacionDetalleRestController(CotizacionDetalleService cotizacionDetalleService) {
		this.cotizacionDetalleService = cotizacionDetalleService;
	}

	@GetMapping("/cotizaciones-detalles")
	public ResponseEntity<?> findAll() {
		ApiResponse response = cotizacionDetalleService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cotizaciones-detalles/cotizacion/{idCotizacion}/item/{item}")
	public ResponseEntity<?> findById(@PathVariable Long idCotizacion, @PathVariable Integer item) {
		ApiResponse response;
		try {
			response = cotizacionDetalleService.searchByIdCotizacionAndItem(idCotizacion, item);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/cotizaciones-detalles")
	public ResponseEntity<?> create(@RequestBody String request) {
		ApiResponse response;
		try {
			response = cotizacionDetalleService.saveOrUpdate(request);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.PRECONDITION_FAILED);
		}
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/cotizaciones-detalles/cotizacion/{idCotizacion}/item/{item}")
	public ResponseEntity<?> delete(@PathVariable Long idCotizacion, @PathVariable Integer item) {
		ApiResponse response;
		try {
			response = cotizacionDetalleService.delete(idCotizacion, item);
		} catch (ApiException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ErrorResponse.of(e.getCode(), e.getMessage(), e.getDetailMessage()), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(response);
	}

}
