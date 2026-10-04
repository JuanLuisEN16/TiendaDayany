package grace_by_dayany.controller;

import grace_by_dayany.service.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CatalogoService catalogo;

    public HomeController(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    @GetMapping("/")
    public String iniciado(Model model) {
        model.addAttribute("activo", "inicio");
        model.addAttribute("destacados", catalogo.destacados());
        return "index";
    }

    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("activo", "nosotros");
        model.addAttribute("titulo", "Nosotros");
        model.addAttribute("texto", "Grace by Dayany nace para vestir a la mujer peruana con blusas y jeans "
                + "que resaltan su esencia. Prendas cómodas, femeninas y listas para cada momento de tu vida.");
        return "pagina";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("activo", "contacto");
        model.addAttribute("titulo", "Contacto");
        model.addAttribute("texto", "¿Tienes dudas sobre una prenda, tallas o tu pedido? "
                + "Escríbenos por WhatsApp y te atendemos de forma personalizada.");
        return "pagina";
    }
}
