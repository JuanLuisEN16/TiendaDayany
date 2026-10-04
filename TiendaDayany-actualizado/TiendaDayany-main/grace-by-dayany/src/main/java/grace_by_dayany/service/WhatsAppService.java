package grace_by_dayany.service;

import grace_by_dayany.model.Carrito;
import grace_by_dayany.model.ItemCarrito;
import grace_by_dayany.model.PedidoForm;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    private final String numero;

    public WhatsAppService(@Value("${app.whatsapp.numero:51999999999}") String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    /** Enlace general para el botón "Escríbenos por WhatsApp". */
    public String enlaceContacto() {
        return enlace("Hola Grace by Dayany, quisiera más información.");
    }

    public String enlacePedido(Carrito carrito, PedidoForm f) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola Grace by Dayany, quiero hacer este pedido:\n\n");
        for (ItemCarrito i : carrito.getItems()) {
            sb.append("• ").append(i.getNombre())
              .append(" (Talla ").append(i.getTalla()).append(", ").append(i.getColor()).append(")")
              .append(" x").append(i.getCantidad())
              .append(" - S/ ").append(i.getSubtotal().setScale(2)).append("\n");
        }
        sb.append("\nTotal: S/ ").append(carrito.getTotal().setScale(2)).append("\n");
        sb.append("Método de pago: ").append(f.getMetodoPago().equals("Visa") ? "Tarjeta de crédito / débito" : "Yape").append("\n\n");
        sb.append("Nombre: ").append(f.getNombre().trim()).append("\n");
        sb.append("Celular: ").append(f.getCelular().trim()).append("\n");
        sb.append("Dirección: ").append(f.getDireccion().trim()).append("\n");
        sb.append("Distrito: ").append(f.getDistrito()).append("\n");
        return enlace(sb.toString());
    }

    public String enlaceProducto(String nombre, String talla, String color, int cantidad) {
        return enlace("Hola Grace by Dayany, quiero comprar: " + nombre + " (Talla " + talla + ", " + color
                + ") x" + cantidad + ".");
    }

    private String enlace(String mensaje) {
        String texto = URLEncoder.encode(mensaje, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://wa.me/" + numero + "?text=" + texto;
    }
}
