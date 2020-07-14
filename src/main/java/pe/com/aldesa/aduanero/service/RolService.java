package pe.com.aldesa.aduanero.service;

import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.exception.ApiException;

public interface RolService {

	ApiResponse findAll() throws ApiException;
	
	ApiResponse findById(Integer id) throws ApiException;

	ApiResponse save(String request) throws ApiException;
	
	ApiResponse update(String request) throws ApiException;

	ApiResponse delete(Integer id) throws ApiException;
	
}
