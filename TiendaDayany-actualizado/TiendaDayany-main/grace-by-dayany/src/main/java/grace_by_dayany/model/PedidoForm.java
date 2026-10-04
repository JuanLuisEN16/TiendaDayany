package grace_by_dayany.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PedidoForm {

    @NotBlank(message = "Ingresa tu nombre completo")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "Ingresa tu número de celular")
    @Pattern(regexp = "^9\\d{2}\\s?\\d{3}\\s?\\d{3}$", message = "Ingresa un celular válido de 9 dígitos (ej: 912 345 678)")
    private String celular;

    @NotBlank(message = "Ingresa tu dirección de envío")
    @Size(max = 200, message = "Máximo 200 caracteres")
    private String direccion;

    @NotBlank(message = "Selecciona un distrito")
    private String distrito;

    @NotBlank(message = "Selecciona un método de pago")
    @Pattern(regexp = "^(Visa|Yape)$", message = "Método de pago no válido")
    private String metodoPago = "Visa";

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
