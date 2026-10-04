package grace_by_dayany.config;

import grace_by_dayany.model.Producto;
import grace_by_dayany.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Carga productos de ejemplo la primera vez (si la tabla está vacía). */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final String ROSA = "Rosa:#f4a0b5";
    private static final String NEGRO = "Negro:#1d1d1f";
    private static final String BLANCO = "Blanco:#ffffff";
    private static final String CELESTE = "Celeste:#6f9be8";
    private static final String LILA = "Lila:#c3a3e6";
    private static final String BEIGE = "Beige:#d9bfa3";
    private static final String GRIS = "Gris:#8d8d90";
    private static final String MARRON = "Marrón:#c9a27c";
    private static final String AZUL = "Azul:#1f4e9a";
    private static final String AZUL_CLARO = "Azul claro:#7fb0e0";
    private static final String AZUL_OSCURO = "Azul oscuro:#12306b";

    private final ProductoRepository repo;

    public DataSeeder(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        List<Producto> lista = new ArrayList<>();

        // Blusas
        lista.add(blusa("Blusa manga corta", 59, ROSA + "," + NEGRO + "," + BLANCO, true));
        lista.add(blusa("Blusa escote cuadrado", 65, NEGRO + "," + BLANCO + "," + ROSA, true));
        lista.add(blusa("Blusa lazo", 69, BLANCO + "," + NEGRO + "," + ROSA, true));
        lista.add(blusa("Blusa rayas celeste", 69, CELESTE + "," + BLANCO, true));
        lista.add(blusa("Blusa manga larga", 75, BLANCO + "," + BEIGE + "," + NEGRO, false));
        lista.add(blusa("Blusa volantes", 65, NEGRO + "," + BLANCO + "," + ROSA, false));
        lista.add(blusa("Blusa amarre frontal", 69, ROSA + "," + BLANCO + "," + CELESTE, false));
        lista.add(blusa("Blusa manga globo", 69, LILA + "," + BLANCO + "," + NEGRO, false));
        lista.add(blusa("Blusa satinada", 79, BEIGE + "," + NEGRO + "," + LILA, false));
        lista.add(blusa("Blusa hombros descubiertos", 62, ROSA + "," + BLANCO, false));
        lista.add(blusa("Blusa crop tejida", 55, BEIGE + "," + BLANCO + "," + GRIS, false));
        lista.add(blusa("Blusa cuello V", 59, NEGRO + "," + GRIS + "," + CELESTE, false));
        lista.add(blusa("Blusa bordada", 85, BLANCO + "," + CELESTE, false));
        lista.add(blusa("Blusa camisera", 72, CELESTE + "," + BLANCO + "," + BEIGE, false));
        lista.add(blusa("Blusa drapeada", 68, LILA + "," + NEGRO, false));
        lista.add(blusa("Blusa top halter", 49, NEGRO + "," + ROSA + "," + BLANCO, false));

        Producto blazer = new Producto();
        blazer.setNombre("Solene Blazer");
        blazer.setCategoria("Blusas");
        blazer.setPrecio(BigDecimal.valueOf(199));
        blazer.setTallas("S,M,L,XL,XXL");
        blazer.setColores(MARRON + "," + BEIGE + "," + GRIS + "," + BLANCO);
        blazer.setDescripcion("Blazer de talle sutil y preciso, el blazer Solene es una prenda moderna y esencial. "
                + "Confeccionado en una mezcla de lino y algodón, ofrece un acabado ligero y una estructura "
                + "relajada que lo hace ideal para el día a día o para un look más formal.");
        blazer.setCalce("Relajado, estructura silueta");
        blazer.setTela("Lino y algodón");
        blazer.setComposicion("55% Lino, 45% Algodón");
        blazer.setCuidados("Lavar a mano o en ciclo delicado. No usar secadora.");
        blazer.setNuevo(true);
        blazer.setRating(4.8);
        blazer.setOpiniones(24);
        lista.add(blazer);

        // Jeans
        lista.add(jean("Jean clásico", 89, AZUL + "," + AZUL_OSCURO + "," + AZUL_CLARO, true));
        lista.add(jean("Jean mom fit", 95, AZUL_OSCURO + "," + AZUL + "," + AZUL_CLARO, true));
        lista.add(jean("Jean tiro alto", 92, AZUL + "," + NEGRO, false));
        lista.add(jean("Jean wide leg", 99, AZUL_CLARO + "," + AZUL_OSCURO, false));
        lista.add(jean("Jean recto", 89, AZUL + "," + GRIS, false));
        lista.add(jean("Jean skinny", 85, NEGRO + "," + AZUL_OSCURO, false));
        lista.add(jean("Jean cargo", 105, BEIGE + "," + AZUL, false));
        lista.add(jean("Jean acampanado", 98, AZUL + "," + AZUL_CLARO, false));
        lista.add(jean("Jean rasgado", 92, AZUL_CLARO + "," + AZUL_OSCURO + "," + NEGRO, false));
        lista.add(jean("Jean paperbag", 95, AZUL + "," + BEIGE, false));
        lista.add(jean("Jean blanco", 89, BLANCO + "," + BEIGE, false));
        lista.add(jean("Jean culotte", 93, AZUL_OSCURO + "," + AZUL_CLARO, false));

        repo.saveAll(lista);
    }

    private Producto blusa(String nombre, int precio, String colores, boolean destacado) {
        Producto p = base(nombre, "Blusas", precio, "S,M,L", colores, destacado);
        p.setDescripcion("Blusa femenina de tela suave y fresca, pensada para combinar con tus jeans favoritos.");
        p.setCalce("Regular");
        p.setTela("Algodón suave");
        p.setComposicion("95% Algodón, 5% Elastano");
        p.setCuidados("Lavar a mano o en ciclo delicado. No usar secadora.");
        return p;
    }

    private Producto jean(String nombre, int precio, String colores, boolean destacado) {
        Producto p = base(nombre, "Jeans", precio, "28,30,32,34", colores, destacado);
        p.setDescripcion("Jean cómodo y resistente con un calce que estiliza. Combínalo con tus blusas Grace.");
        p.setCalce("Ajustado a la cintura");
        p.setTela("Denim");
        p.setComposicion("98% Algodón, 2% Elastano");
        p.setCuidados("Lavar del revés con agua fría. No usar blanqueador.");
        return p;
    }

    private Producto base(String nombre, String categoria, int precio, String tallas, String colores, boolean destacado) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setCategoria(categoria);
        p.setPrecio(BigDecimal.valueOf(precio));
        p.setTallas(tallas);
        p.setColores(colores);
        p.setDestacado(destacado);
        p.setRating(4.5);
        p.setOpiniones(10 + (nombre.length() * 3) % 40);
        return p;
    }
}
