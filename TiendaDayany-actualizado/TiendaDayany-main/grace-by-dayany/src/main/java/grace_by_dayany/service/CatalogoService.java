package grace_by_dayany.service;

import grace_by_dayany.model.Producto;
import grace_by_dayany.repository.ProductoRepository;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService {

    public static final int POR_PAGINA = 12;

    public static class Resultado {
        private final List<Producto> productos;
        private final int total;
        private final int pagina;
        private final int paginas;

        public Resultado(List<Producto> productos, int total, int pagina, int paginas) {
            this.productos = productos;
            this.total = total;
            this.pagina = pagina;
            this.paginas = paginas;
        }

        public List<Producto> getProductos() { return productos; }
        public int getTotal() { return total; }
        public int getPagina() { return pagina; }
        public int getPaginas() { return paginas; }
    }

    private final ProductoRepository repo;

    public CatalogoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public Resultado buscar(String categoria, List<String> tallas, List<String> colores,
                            String precio, String q, String orden, int pagina) {

        List<Producto> filtrados = repo.findAll().stream()
                .filter(p -> categoria == null || categoria.isBlank() || p.getCategoria().equalsIgnoreCase(categoria))
                .filter(p -> tallas == null || tallas.isEmpty()
                        || p.getTallasLista().stream().anyMatch(tallas::contains))
                .filter(p -> colores == null || colores.isEmpty()
                        || p.getColoresLista().stream().anyMatch(c -> colores.contains(c.getNombre())))
                .filter(p -> enRango(p.getPrecio(), precio))
                .filter(p -> q == null || q.isBlank() || normalizar(p.getNombre()).contains(normalizar(q)))
                .sorted(comparador(orden))
                .collect(Collectors.toList());

        int total = filtrados.size();
        int paginas = Math.max(1, (int) Math.ceil(total / (double) POR_PAGINA));
        int actual = Math.min(Math.max(1, pagina), paginas);
        int desde = (actual - 1) * POR_PAGINA;
        List<Producto> pagina_ = filtrados.subList(Math.min(desde, total), Math.min(desde + POR_PAGINA, total));
        return new Resultado(pagina_, total, actual, paginas);
    }

    /** Colores distintos de todo el catálogo (para el filtro). */
    public List<Producto.Color> coloresDisponibles() {
        Map<String, Producto.Color> mapa = new LinkedHashMap<>();
        for (Producto p : repo.findAll()) {
            for (Producto.Color c : p.getColoresLista()) mapa.putIfAbsent(c.getNombre(), c);
        }
        return new ArrayList<>(mapa.values());
    }

    public List<Producto> destacados() {
        return repo.findByDestacadoTrueOrderByIdAsc().stream().limit(6).toList();
    }

    public Optional<Producto> porId(Long id) {
        return repo.findById(id);
    }

    public List<Producto> relacionados(Producto p) {
        return repo.findByCategoriaIgnoreCaseAndIdNotOrderByIdDesc(p.getCategoria(), p.getId())
                .stream().limit(4).toList();
    }

    public long contar() {
        return repo.count();
    }

    private boolean enRango(BigDecimal precio, String rango) {
        if (rango == null || rango.isBlank()) return true;
        double v = precio.doubleValue();
        return switch (rango) {
            case "0-60" -> v <= 60;
            case "60-90" -> v > 60 && v <= 90;
            case "90+" -> v > 90;
            default -> true;
        };
    }

    private Comparator<Producto> comparador(String orden) {
        if (orden == null) orden = "recientes";
        return switch (orden) {
            case "precio_asc" -> Comparator.comparing(Producto::getPrecio);
            case "precio_desc" -> Comparator.comparing(Producto::getPrecio).reversed();
            case "nombre" -> Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(Producto::getId).reversed();
        };
    }

    private String normalizar(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }
}
