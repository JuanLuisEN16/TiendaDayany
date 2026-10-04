package grace_by_dayany;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class TiendaFlujoTests {

    @Autowired
    WebApplicationContext ctx;

    MockMvc mvc() {
        return MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    void paginasPublicas() throws Exception {
        MockMvc m = mvc();
        for (String url : new String[]{"/", "/catalogo", "/catalogo?categoria=Jeans&talla=30&orden=precio_asc&page=1",
                "/catalogo?q=lazo", "/producto/1", "/carrito", "/nosotros", "/contacto", "/favoritos",
                "/favoritos/lista?ids=1,2"}) {
            m.perform(get(url)).andExpect(status().isOk());
        }
    }

    @Test
    void flujoCarritoYWhatsApp() throws Exception {
        MockMvc m = mvc();
        MockHttpSession s = new MockHttpSession();

        m.perform(post("/carrito/agregar").session(s).param("id", "1").param("talla", "M").param("color", "Rosa").param("cantidad", "2"))
                .andExpect(redirectedUrl("/carrito"));
        m.perform(post("/carrito/agregar-rapido").session(s).param("id", "2"))
                .andExpect(status().isOk()).andExpect(content().string("3"));
        m.perform(get("/carrito").session(s)).andExpect(status().isOk())
                .andExpect(r -> assertTrue(r.getResponse().getContentAsString().contains("Resumen del pedido")));

        // datos inválidos -> vuelve al formulario con errores
        m.perform(post("/carrito/enviar").session(s).param("nombre", "").param("celular", "123"))
                .andExpect(status().isOk())
                .andExpect(r -> assertTrue(r.getResponse().getContentAsString().contains("Ingresa tu nombre completo")));

        // datos válidos -> confirmación con enlace de WhatsApp
        m.perform(post("/carrito/enviar").session(s).param("nombre", "Ana Torres").param("celular", "912 345 678")
                        .param("direccion", "Av. Principal 123").param("distrito", "Los Olivos").param("metodoPago", "Yape"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/pedido/confirmado"))
                .andExpect(flash().attributeExists("waUrl"));
    }
}
