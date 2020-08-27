package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.Menu;
import pe.com.aldesa.aduanero.repository.MenuRepository;

@Service
public class MenuService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuRepository menuRepository;

	public ApiResponse findAll() {
		List<Menu> menus = menuRepository.findAll();
		int total = menus.size();
		logger.debug("Total Menus: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), menus, total);
	}

}
