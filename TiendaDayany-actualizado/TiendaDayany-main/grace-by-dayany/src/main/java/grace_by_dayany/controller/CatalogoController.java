package grace_by_dayany.controller;

import grace_by_dayany.model.Producto;
import grace_by_dayany.service.CatalogoService;
import grace_by_dayany.service.WhatsAppService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoController {

    private final CatalogoService catalogo;
    private final WhatsAppService whatsapp;

    public CatalogoController(CatalogoService catalogo, WhatsAppService whatsapp) {
        this.catalogo = catalogo;
        this.whatsapp = whatsapp;
    }

    @GetMapping("/blusas")
    public String blusas() {
        return "redirect:/catalogo?categoria=Blusas";
    }

    @GetMapping("/jeans")
    public String jeans() {
        return "redirect:/catalogo?categoria=Jeans";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String categoria,
                           @RequestParam(required = false) List<String> talla,
                           @RequestParam(required = false) List<String> color,
                           @RequestParam(required = false) String precio,
                           @RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "recientes") String orden,
                           @RequestParam(defaultValue = "1") int page,
                           Model model) {

        CatalogoService.Resultado r = catalogo.buscar(categoria, talla, color, precio, q, orden, page);

        String activo = "catalogo";
        if ("Blusas".equalsIgnoreCase(categoria)) activo = "blusas";
        if ("Jeans".equalsIgnoreCase(categoria)) activo = "jeans";

        model.addAttribute("activo", activo);
        model.addAttribute("resultado", r);
        model.addAttribute("categoria", categoria == null ? "" : categoria);
        model.addAttribute("tallasSel", talla == null ? List.of() : talla);
        model.addAttribute("coloresSel", color == null ? List.of() : color);
        model.addAttribute("precio", precio == null ? "" : precio);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("orden", orden);
        model.addAttribute("coloresDisponibles", catalogo.coloresDisponibles());
        model.addAttribute("tallasDisponibles", List.of("S", "M", "L", "28", "30", "32", "34"));
        return "catalogo";
    }

    @GetMapping("/producto/{id}")
    public String producto(@PathVariable Long id, Model model) {
        Producto p = catalogo.porId(id).orElse(null);
        if (p == null) return "redirect:/catalogo";
        model.addAttribute("activo", "catalogo");
        model.addAttribute("p", p);
        model.addAttribute("relacionados", catalogo.relacionados(p));
        model.addAttribute("waNumero", whatsapp.getNumero());
        return "producto";
    }

    @GetMapping("/favoritos")
    public String favoritos(Model model) {
        model.addAttribute("activo", "favoritos");
        return "favoritos";
    }

    /** Devuelve las tarjetas de los productos guardados como favoritos (ids guardados en el navegador). */
    @GetMapping("/favoritos/lista")
    public String listaFavoritos(@RequestParam(defaultValue = "") List<Long> ids, Model model) {
        List<Producto> productos = ids.stream().limit(60)
                .map(catalogo::porId).flatMap(java.util.Optional::stream).toList();
        model.addAttribute("productos", productos);
        return "fragments/favoritos-lista :: lista";
    }
}
