package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.*;

public class PedidoTest {

    private Pedido pedido;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private Combo combo;
    private ProductoAjustado hamburguesaAjustada;

    @BeforeEach
    public void setUp() {
        // Productos base
        hamburguesa = new ProductoMenu("Hamburguesa", 10000);
        papas = new ProductoMenu("Papas", 5000);
        gaseosa = new ProductoMenu("Gaseosa", 3000);

        // Combo (20% de descuento)
        ArrayList<ProductoMenu> itemsCombo = new ArrayList<>();
        itemsCombo.add(papas);
        itemsCombo.add(gaseosa);
        combo = new Combo("Combo Papas-Gaseosa", 0.8, itemsCombo); // 20% de descuento aplicado

        // Producto ajustado (agregando ingrediente)
        Ingrediente extraQueso = new Ingrediente("Queso", 2000);
        hamburguesaAjustada = new ProductoAjustado(hamburguesa);
        // Simulamos agregar manualmente el ingrediente extra
        try {
            var campoAgregados = ProductoAjustado.class.getDeclaredField("agregados");
            campoAgregados.setAccessible(true);
            ArrayList<Ingrediente> listaAgregados = new ArrayList<>();
            listaAgregados.add(extraQueso);
            campoAgregados.set(hamburguesaAjustada, listaAgregados);
        } catch (Exception e) {
            fail("No se pudieron simular los ingredientes del producto ajustado");
        }

        // Pedido
        pedido = new Pedido("Sergio Quiroga", "Calle 123");
        pedido.agregarProducto(hamburguesaAjustada);
        pedido.agregarProducto(combo);
    }

    @Test
    public void testGetIdPedidoIncrementa() {
        Pedido otroPedido = new Pedido("Cliente 2", "Calle 999");
        assertTrue(otroPedido.getIdPedido() == 1+ pedido.getIdPedido(),
                "Cada pedido nuevo debe tener un ID mayor al anterior");
    }

    @Test
    public void testPrecioTotalConTiposDiferentes() {
        // Precios esperados
        int precioHamburguesa = 10000 + 2000; // con extra queso
        int precioCombo = (int) ((5000 + 3000) * 0.8); // descuento aplicado
        int neto = precioHamburguesa + precioCombo;
        int iva = (int) (neto * 0.19);
        int totalEsperado = neto + iva;

        assertEquals(totalEsperado, pedido.getPrecioTotalPedido(),
                "El total del pedido con productos variados (menú, combo, ajustado) debe ser correcto");
    }

    @Test
    public void testFacturaIncluyeTodosLosProductos() {
        String factura = pedido.generarTextoFactura();

        assertTrue(factura.contains("Sergio Quiroga"), "Debe incluir el nombre del cliente");
        assertTrue(factura.contains("Calle 123"), "Debe incluir la dirección del cliente");
        assertTrue(factura.contains("hamburguesa"), "Debe incluir la hamburguesa ajustada");
        assertTrue(factura.contains("Combo"), "Debe incluir el combo");
        assertTrue(factura.contains("Precio Total"), "Debe incluir el total del pedido");
    }

    @Test
    public void testGuardarFacturaGeneraArchivo() throws FileNotFoundException {
        File archivo = new File("factura_integracion_test.txt");

        if (archivo.exists()) archivo.delete();

        pedido.guardarFactura(archivo);

        assertTrue(archivo.exists(), "El archivo de factura debería generarse correctamente");
        assertTrue(archivo.length() > 0, "El archivo no debe estar vacío");

        archivo.delete(); // limpieza
    }
}
