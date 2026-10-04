package grace_by_dayany.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemCarrito implements Serializable {

    private final Long productoId;
    private final String nombre;
    private final BigDecimal precio;
    private final String talla;
    private final String color;
    private final String colorHex;
    private final String imagen;
    private int cantidad;

    public ItemCarrito(Producto p, String talla, String color, String colorHex, int cantidad) {
        this.productoId = p.getId();
        this.nombre = p.getNombre();
        this.precio = p.getPrecio();
        this.imagen = p.getImagen();
        this.talla = talla;
        this.color = color;
        this.colorHex = colorHex;
        this.cantidad = cantidad;
    }

    public boolean esMismo(Long productoId, String talla, String color) {
        return this.productoId.equals(productoId) && this.talla.equals(talla) && this.color.equals(color);
    }

    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public String getTalla() { return talla; }
    public String getColor() { return color; }
    public String getColorHex() { return colorHex; }
    public String getImagen() { return imagen; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
