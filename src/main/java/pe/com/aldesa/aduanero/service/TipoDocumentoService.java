package pe.com.aldesa.aduanero.service;

import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;

public interface TipoDocumentoService {
	
	ApiResponse findAll() throws ApiException;
	
	ApiResponse save(TipoDocumento tipoDocumento) throws ApiException;

	ApiResponse delete(Integer id) throws ApiException;
	
	ApiResponse findById(Integer id) throws ApiException;

}
