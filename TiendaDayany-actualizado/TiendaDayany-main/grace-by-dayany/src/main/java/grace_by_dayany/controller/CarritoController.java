package grace_by_dayany.controller;

import grace_by_dayany.model.Carrito;
import grace_by_dayany.model.PedidoForm;
import grace_by_dayany.model.Producto;
import grace_by_dayany.service.CatalogoService;
import grace_by_dayany.service.WhatsAppService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarritoController {

    static final List<String> DISTRITOS = List.of(
            "Ate", "Barranco", "Breña", "Carabayllo", "Chorrillos", "Comas", "El Agustino",
            "Independencia", "Jesús María", "La Molina", "La Victoria", "Lima Cercado", "Lince",
            "Los Olivos", "Magdalena", "Miraflores", "Pueblo Libre", "Puente Piedra", "Rímac",
            "San Borja", "San Isidro", "San Juan de Lurigancho", "San Juan de Miraflores",
            "San Luis", "San Martín de Porres", "San Miguel", "Santa Anita", "Santiago de Surco",
            "Surquillo", "Villa El Salvador", "Villa María del Triunfo", "Otro distrito");

    private final Carrito carrito;
    private final CatalogoService catalogo;
    private final WhatsAppService whatsapp;

    public CarritoController(Carrito carrito, CatalogoService catalogo, WhatsAppService whatsapp) {
        this.carrito = carrito;
        this.catalogo = catalogo;
        this.whatsapp = whatsapp;
    }

    @GetMapping("/carrito")
    public String ver(Model model) {
        prepararVista(model);
        if (!model.containsAttribute("pedido")) model.addAttribute("pedido", new PedidoForm());
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregar(@RequestParam Long id,
                          @RequestParam(required = false) String talla,
                          @RequestParam(required = false) String color,
                          @RequestParam(defaultValue = "1") int cantidad,
                          @RequestParam(defaultValue = "carrito") String destino,
                          RedirectAttributes ra) {
        Producto p = catalogo.porId(id).orElse(null);
        if (p == null) return "redirect:/catalogo";

        int cant = Math.max(1, cantidad);
        String t = tallaValida(p, talla);
        Producto.Color c = colorValido(p, color);

        // "Comprar por WhatsApp" abre el chat directo, sin tocar el carrito.
        if ("whatsapp".equals(destino)) {
            return "redirect:" + whatsapp.enlaceProducto(p.getNombre(), t, c.getNombre(), cant);
        }

        carrito.agregar(p, t, c.getNombre(), c.getHex(), cant);
        if ("seguir".equals(destino)) {
            ra.addFlashAttribute("aviso", p.getNombre() + " se agregó al carrito");
            return "redirect:/producto/" + id;
        }
        return "redirect:/carrito";
    }

    /** Usado por JavaScript desde las tarjetas del catálogo: devuelve la nueva cantidad total. */
    @PostMapping("/carrito/agregar-rapido")
    @ResponseBody
    public ResponseEntity<String> agregarRapido(@RequestParam Long id) {
        Producto p = catalogo.porId(id).orElse(null);
        if (p == null) return ResponseEntity.notFound().build();
        Producto.Color c = colorValido(p, null);
        carrito.agregar(p, tallaValida(p, null), c.getNombre(), c.getHex(), 1);
        return ResponseEntity.ok(String.valueOf(carrito.getTotalUnidades()));
    }

    private String tallaValida(Producto p, String talla) {
        return (talla != null && p.getTallasLista().contains(talla)) ? talla : p.getTallasLista().get(0);
    }

    private Producto.Color colorValido(Producto p, String color) {
        return p.getColoresLista().stream()
                .filter(x -> x.getNombre().equals(color)).findFirst()
                .orElse(p.getColoresLista().get(0));
    }

    @PostMapping("/carrito/cambiar")
    public String cambiar(@RequestParam int indice, @RequestParam int delta) {
        carrito.cambiarCantidad(indice, delta);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminar(@RequestParam int indice) {
        carrito.eliminar(indice);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/enviar")
    public String enviar(@Valid @ModelAttribute("pedido") PedidoForm pedido,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (carrito.isVacio()) return "redirect:/carrito";
        if (!DISTRITOS.contains(pedido.getDistrito() == null ? "" : pedido.getDistrito())
                && !result.hasFieldErrors("distrito")) {
            result.rejectValue("distrito", "distrito.invalido", "Selecciona un distrito de la lista");
        }
        if (result.hasErrors()) {
            prepararVista(model);
            return "carrito";
        }
        String url = whatsapp.enlacePedido(carrito, pedido);
        double total = carrito.getTotal().doubleValue();
        carrito.vaciar();
        ra.addFlashAttribute("waUrl", url);
        ra.addFlashAttribute("totalPedido", total);
        return "redirect:/pedido/confirmado";
    }

    @GetMapping("/pedido/confirmado")
    public String confirmado(Model model) {
        if (!model.containsAttribute("waUrl")) return "redirect:/";
        model.addAttribute("activo", "");
        return "confirmado";
    }

    private void prepararVista(Model model) {
        model.addAttribute("activo", "carrito");
        model.addAttribute("carrito", carrito);
        model.addAttribute("distritos", DISTRITOS);
    }
}
