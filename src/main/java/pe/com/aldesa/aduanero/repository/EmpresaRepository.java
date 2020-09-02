package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	Empresa findByRuc(String ruc);

	List<Empresa> findByRazonSocial(String razonsocial);

	List<Empresa> findByNombreComercial(String nombrecomercial);

	@Override
	Page<Empresa> findAll(Pageable pageable);

}
