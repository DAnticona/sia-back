package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

}
