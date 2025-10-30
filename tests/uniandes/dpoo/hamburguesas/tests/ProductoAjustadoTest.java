package uniandes.dpoo.hamburguesas.tests;
//prueba commit
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.*;
import java.util.ArrayList;

class ProductoAjustadoTest {

    private ProductoMenu productoBase;
    private ProductoAjustado productoAjustado;
    private Ingrediente queso;
    private Ingrediente tocineta;

    @BeforeEach
    void setUp() {
        productoBase = new ProductoMenu("Hamburguesa Clásica", 10000);
        productoAjustado = new ProductoAjustado(productoBase);

        queso = new Ingrediente("Queso", 2000);
        tocineta = new Ingrediente("Tocineta", 3000);
    }

    @Test
    void testGetNombre() {
        assertEquals("Hamburguesa Clásica", productoAjustado.getNombre(),
                "El nombre del producto ajustado debe ser el del producto base");
    }

    @Test
    void testGetPrecioSinAgregados() {
       
        assertEquals(10000, productoAjustado.getPrecio(),
                "El precio sin agregados debería ser igual al del producto base");
    }

    @Test
    void testGenerarTextoFacturaSinAgregados() {
        String texto = productoAjustado.generarTextoFactura();
        assertTrue(texto.contains("Hamburguesa Clásica"),
                "El texto debe incluir el nombre del producto base");
        assertTrue(texto.contains(String.valueOf(productoAjustado.getPrecio())),
               "El texto debe incluir el precio real del producto ajustado");
            }

    @Test
    void testGenerarTextoFacturaConAgregadosYEliminados() {
    	 productoAjustado.agregarIngrediente(queso);
    	    productoAjustado.agregarIngrediente(tocineta);
    	    productoAjustado.eliminarIngrediente(new Ingrediente("Lechuga", 0));

    	    String texto = productoAjustado.generarTextoFactura();

    	    assertTrue(texto.contains("+Queso"), "Debelistar el ingrediente agregado Queso");
    	    assertTrue(texto.contains("+Tocineta"), "Debe listar el ingrediente agregadoo Tocineta");
    	    assertTrue(texto.contains("-Lechuga"), "Debe listar el ingrediente eliminado Lechuga");
    	}
}
