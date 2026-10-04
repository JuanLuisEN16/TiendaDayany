package grace_by_dayany.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/** Carrito de compras: uno por sesión de navegador. */
@Component
@SessionScope
public class Carrito implements Serializable {

    public static final int MAX_POR_ITEM = 10;

    private final List<ItemCarrito> items = new ArrayList<>();

    public void agregar(Producto p, String talla, String color, String colorHex, int cantidad) {
        for (ItemCarrito i : items) {
            if (i.esMismo(p.getId(), talla, color)) {
                i.setCantidad(Math.min(MAX_POR_ITEM, i.getCantidad() + cantidad));
                return;
            }
        }
        items.add(new ItemCarrito(p, talla, color, colorHex, Math.min(MAX_POR_ITEM, cantidad)));
    }

    public void cambiarCantidad(int indice, int delta) {
        if (indice < 0 || indice >= items.size()) return;
        ItemCarrito i = items.get(indice);
        int nueva = i.getCantidad() + delta;
        if (nueva < 1) nueva = 1;
        if (nueva > MAX_POR_ITEM) nueva = MAX_POR_ITEM;
        i.setCantidad(nueva);
    }

    public void eliminar(int indice) {
        if (indice >= 0 && indice < items.size()) items.remove(indice);
    }

    public void vaciar() {
        items.clear();
    }

    public List<ItemCarrito> getItems() { return items; }

    public int getTotalUnidades() {
        return items.stream().mapToInt(ItemCarrito::getCantidad).sum();
    }

    public boolean isVacio() { return items.isEmpty(); }

    public BigDecimal getSubtotal() {
        return items.stream().map(ItemCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDescuento() { return BigDecimal.ZERO; }

    public BigDecimal getEnvio() { return BigDecimal.ZERO; }

    public BigDecimal getTotal() {
        return getSubtotal().subtract(getDescuento()).add(getEnvio());
    }
}
