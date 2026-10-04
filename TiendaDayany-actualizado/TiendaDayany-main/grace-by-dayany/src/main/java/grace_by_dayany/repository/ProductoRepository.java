package grace_by_dayany.repository;

import grace_by_dayany.model.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByDestacadoTrueOrderByIdAsc();

    List<Producto> findByCategoriaIgnoreCaseAndIdNotOrderByIdDesc(String categoria, Long id);
}
