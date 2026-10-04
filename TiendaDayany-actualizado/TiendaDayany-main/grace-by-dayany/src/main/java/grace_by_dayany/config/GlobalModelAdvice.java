package grace_by_dayany.config;

import grace_by_dayany.model.Carrito;
import grace_by_dayany.service.WhatsAppService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Datos que necesitan todas las páginas (contador del carrito, WhatsApp). */
@ControllerAdvice
public class GlobalModelAdvice {

    private final Carrito carrito;
    private final WhatsAppService whatsapp;

    public GlobalModelAdvice(Carrito carrito, WhatsAppService whatsapp) {
        this.carrito = carrito;
        this.whatsapp = whatsapp;
    }

    @ModelAttribute("cartCount")
    public int cartCount() {
        return carrito.getTotalUnidades();
    }

    @ModelAttribute("waContacto")
    public String waContacto() {
        return whatsapp.enlaceContacto();
    }
}
