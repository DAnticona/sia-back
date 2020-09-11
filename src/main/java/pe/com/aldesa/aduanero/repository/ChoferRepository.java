package pe.com.aldesa.aduanero.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Chofer;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {

	@Override
	Page<Chofer> findAll(Pageable pageable);

}
