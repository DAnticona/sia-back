package pe.com.aldesa.aduanero.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	Empresa findByRuc(String ruc);

	@Query("select e from Empresa e where e.razonSocial like %:razonSocial% or "
			+ "e.nombreComercial like %:razonSocial%")
	List<Empresa> findByRazonSocial(String razonSocial);

	@Override
	Page<Empresa> findAll(Pageable pageable);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Empresa e set e.ruc = :ruc, e.razonSocial = :razonSocial, e.nombreComercial = :nombreComercial, e.direccion = :direccion"
			+ " where e.idEmpresa = :idEmpresa")
	void updateEmpresa(Long idEmpresa, String ruc, String razonSocial, String nombreComercial, Direccion direccion);

}
