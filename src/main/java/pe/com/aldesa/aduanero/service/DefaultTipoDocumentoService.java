package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;

@Service
public class DefaultTipoDocumentoService implements TipoDocumentoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	public DefaultTipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
		this.tipoDocumentoRepository = tipoDocumentoRepository;
	}
	
	@Override
	public ApiResponse findAll() throws ApiException {
		List<TipoDocumento> listTypeDocuments = tipoDocumentoRepository.findAll();
		logger.debug("Total Tipo de documentos: {}", listTypeDocuments.size());
		if (listTypeDocuments.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return new ApiResponse(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), listTypeDocuments, listTypeDocuments.size());
	}
	
	@Override
	public ApiResponse save(TipoDocumento tipoDocumento) throws ApiException {
		TipoDocumento tmpTypeDocument = null;
		try {
			tipoDocumento.setAbreviatura(tipoDocumento.getAbreviatura().toUpperCase());
			tmpTypeDocument = tipoDocumentoRepository.save(Preconditions.checkNotNull(tipoDocumento));
		} catch (Exception e) {
			logger.error(ApiError.NO_APPLICATION_PROCESSED.getCode() + ": " + ApiError.NO_APPLICATION_PROCESSED.getMessage(), e);
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage());
		}
		return new ApiResponse(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTypeDocument);
	}
	
	@Override
	public ApiResponse delete(Integer id) throws ApiException {
		TipoDocumento tmpTypeDocument = tipoDocumentoRepository.findById(Preconditions.checkNotNull(id)).orElse(null);
		logger.debug("Tipo documento: {}", tmpTypeDocument);
		if (null != tmpTypeDocument) {
			tipoDocumentoRepository.deleteById(id);
			return new ApiResponse(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Tipo Documento " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
	
	@Override
	public ApiResponse findById(Integer id) throws ApiException {
		TipoDocumento tmpTypeDocument = tipoDocumentoRepository.findById(Preconditions.checkNotNull(id)).orElse(null);
		logger.debug("Tipo documento: {}", tmpTypeDocument);
		if (null == tmpTypeDocument) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return new ApiResponse(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTypeDocument);
	}

}
