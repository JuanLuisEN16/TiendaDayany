package grace_by_dayany.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    public static class Color {
        private final String nombre;
        private final String hex;

        public Color(String nombre, String hex) {
            this.nombre = nombre;
            this.hex = hex;
        }

        public String getNombre() { return nombre; }
        public String getHex() { return hex; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    /** "Blusas" o "Jeans". */
    @Column(nullable = false, length = 40)
    private String categoria;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /** Tallas separadas por coma. Ej: "S,M,L". */
    @Column(nullable = false, length = 120)
    private String tallas;

    /** Colores como "Rosa:#f4a0b5,Negro:#1d1d1f". */
    @Column(nullable = false, length = 300)
    private String colores;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 120)
    private String calce;

    @Column(length = 120)
    private String tela;

    @Column(length = 120)
    private String composicion;

    @Column(length = 250)
    private String cuidados;

    /** Ruta de la foto (ej: /img/productos/blusa1.jpg). Si es null se muestra un marcador. */
    @Column(length = 250)
    private String imagen;

    private boolean nuevo;
    private boolean destacado;
    private double rating = 4.5;
    private int opiniones;

    public List<String> getTallasLista() {
        return Arrays.stream(tallas.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    public List<Color> getColoresLista() {
        return Arrays.stream(colores.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    String[] p = s.split(":");
                    return new Color(p[0], p.length > 1 ? p[1] : "#cccccc");
                })
                .toList();
    }

    public String getPrimerColorHex() {
        List<Color> c = getColoresLista();
        return c.isEmpty() ? "#f4c9d4" : c.get(0).getHex();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getTallas() { return tallas; }
    public void setTallas(String tallas) { this.tallas = tallas; }
    public String getColores() { return colores; }
    public void setColores(String colores) { this.colores = colores; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCalce() { return calce; }
    public void setCalce(String calce) { this.calce = calce; }
    public String getTela() { return tela; }
    public void setTela(String tela) { this.tela = tela; }
    public String getComposicion() { return composicion; }
    public void setComposicion(String composicion) { this.composicion = composicion; }
    public String getCuidados() { return cuidados; }
    public void setCuidados(String cuidados) { this.cuidados = cuidados; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isNuevo() { return nuevo; }
    public void setNuevo(boolean nuevo) { this.nuevo = nuevo; }
    public boolean isDestacado() { return destacado; }
    public void setDestacado(boolean destacado) { this.destacado = destacado; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getOpiniones() { return opiniones; }
    public void setOpiniones(int opiniones) { this.opiniones = opiniones; }
}
