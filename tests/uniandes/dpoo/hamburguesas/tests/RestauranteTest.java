package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;

import uniandes.dpoo.hamburguesas.mundo.*;
import uniandes.dpoo.hamburguesas.excepciones.*;

public class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
    }

    // =======================================
    // 🧩 PRUEBAS DE PEDIDOS
    // =======================================

    @Test
    @DisplayName("Debe iniciar un pedido correctamente")
    void testIniciarPedidoCorrectamente() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Sergio", "Calle 123");
        Pedido pedido = restaurante.getPedidoEnCurso();

        assertNotNull(pedido, "Debe existir un pedido en curso");
        assertEquals("Sergio", pedido.getNombreCliente(), "El nombre del cliente debe coincidir");
    }

    @Test
    @DisplayName("Debe lanzar excepción si ya hay un pedido en curso")
    void testYaHayUnPedidoEnCursoException() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Sergio", "Calle 123");

        YaHayUnPedidoEnCursoException ex = assertThrows(
            YaHayUnPedidoEnCursoException.class,
            () -> restaurante.iniciarPedido("Laura", "Calle 456"),
            "Debe lanzar YaHayUnPedidoEnCursoException"
        );

        assertTrue(ex.getMessage().contains("Sergio"));
        assertTrue(ex.getMessage().contains("Laura"));
    }

    @Test
    @DisplayName("Debe lanzar excepción al cerrar pedido cuando no hay uno en curso")
    void testNoHayPedidoEnCursoException() {
        assertThrows(
            NoHayPedidoEnCursoException.class,
            () -> restaurante.cerrarYGuardarPedido(),
            "Debe lanzar NoHayPedidoEnCursoException si no hay pedido activo"
        );
    }

    // =======================================
    // 🧾 PRUEBA DE CIERRE Y GUARDADO
    // =======================================

    @Test
    @DisplayName("Debe cerrar y guardar un pedido correctamente")
    void testCerrarYGuardarPedido() throws Exception {
        restaurante.iniciarPedido("Sergio", "Calle 123");
        Pedido pedido = restaurante.getPedidoEnCurso();
        pedido.agregarProducto(new ProductoMenu("Hamburguesa sencilla", 12000));

        File carpeta = new File("./facturas/");
        if (!carpeta.exists()) carpeta.mkdir();

        restaurante.cerrarYGuardarPedido();

        File factura = new File("./facturas/factura_" + pedido.getIdPedido() + ".txt");
        assertTrue(factura.exists(), "El archivo de factura debe haberse creado");

        // Limpieza (para no dejar archivos sucios)
        factura.delete();
    }

    // =======================================
    // 📄 PRUEBAS DE CARGA DE INFORMACIÓN
    // =======================================

    @Test
    @DisplayName("Debe cargar ingredientes correctamente desde archivo")
    void testCargarIngredientes() throws Exception {
        File archivoIngredientes = crearArchivoTemporal("ingredientes.txt", "Queso;500\nTomate;300\n");

        restaurante.cargarInformacionRestaurante(archivoIngredientes, crearArchivoTemporal("menu.txt", ""), crearArchivoTemporal("combos.txt", ""));
        ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes();

        assertEquals(2, ingredientes.size(), "Debe cargar dos ingredientes");
        assertEquals("Queso", ingredientes.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción si hay ingredientes repetidos")
    void testIngredienteRepetidoException() throws Exception {
        File archivoIngredientes = crearArchivoTemporal("ingredientes.txt", "Queso;500\nQueso;700\n");

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, crearArchivoTemporal("menu.txt", ""), crearArchivoTemporal("combos.txt", ""));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si hay productos base repetidos")
    void testProductoRepetidoExceptionEnMenu() throws Exception {
        File archivoMenu = crearArchivoTemporal("menu.txt", "Papas;5000\nPapas;6000\n");

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ingredientes.txt", ""), archivoMenu, crearArchivoTemporal("combos.txt", ""));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si combo incluye producto inexistente")
    void testProductoFaltanteException() throws Exception {
        File archivoMenu = crearArchivoTemporal("menu.txt", "Hamburguesa;10000\n");
        File archivoCombos = crearArchivoTemporal("combos.txt", "ComboEspecial;10%;Papas\n");

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ingredientes.txt", ""), archivoMenu, archivoCombos);
        });
    }

    // =======================================
    // 🔧 MÉTODO AUXILIAR PARA ARCHIVOS TEMPORALES
    // =======================================

    private File crearArchivoTemporal(String nombre, String contenido) throws IOException {
        File archivo = new File(nombre);
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(contenido);
        }
        return archivo;
    }
    
    //====================lo de arriba que monda?
    
    @Test
    @DisplayName("getPedidoEnCurso debe retornar el pedido actual si existe")
    void testGetPedidoEnCurso() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Sergio", "Calle 123");
        assertNotNull(restaurante.getPedidoEnCurso(), "Debe retornar el pedido en curso actual");
    }

    @Test
    @DisplayName("getPedidoEnCurso debe retornar null si no hay pedido activo")
    void testGetPedidoEnCursoNull() {
        assertNull(restaurante.getPedidoEnCurso(), "Debe retornar null si no hay pedido en curso");
    }

    @Test
    @DisplayName("getPedidos debe iniciar con una lista vacía")
    void testGetPedidos() {
        assertTrue(restaurante.getPedidos().isEmpty(), "La lista de pedidos debe iniciar vacía");
    }

    @Test
    @DisplayName("getMenuBase debe iniciar vacío y poder llenarse al cargar datos")
    void testGetMenuBase() throws Exception {
        File menu = crearArchivoTemporal("menu_test.txt", "Hamburguesa;10000\nPapas;5000\n");
        restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ing_test.txt", ""), menu, crearArchivoTemporal("combos_test.txt", ""));
        assertEquals(2, restaurante.getMenuBase().size(), "El menú base debe tener los productos cargados");
    }

    @Test
    @DisplayName("getMenuCombos debe iniciar vacío")
    void testGetMenuCombosVacio() {
        assertTrue(restaurante.getMenuCombos().isEmpty(), "La lista de combos debe iniciar vacía");
    }

    @Test
    @DisplayName("getIngredientes debe iniciar vacío y llenarse correctamente")
    void testGetIngredientes() throws Exception {
        File ingredientes = crearArchivoTemporal("ing_test.txt", "Queso;200\nTocineta;400\n");
        restaurante.cargarInformacionRestaurante(ingredientes, crearArchivoTemporal("menu_test.txt", ""), crearArchivoTemporal("combos_test.txt", ""));
        assertEquals(2, restaurante.getIngredientes().size(), "Debe cargar correctamente los ingredientes");
    }

    // =======================================
    // ⚠️ TEST DE PRODUCTOREPETIDOEXCEPTION EN COMBOS
    // =======================================

    @Test
    @DisplayName("Debe lanzar ProductoRepetidoException si hay combos repetidos")
    void testProductoRepetidoExceptionEnCombos() throws Exception {
        File archivoMenu = crearArchivoTemporal("menu.txt", "Hamburguesa;10000\nPapas;5000\n");
        File archivoCombos = crearArchivoTemporal("combos.txt",
                "ComboFeliz;10%;Hamburguesa;Papas\nComboFeliz;20%;Hamburguesa;Papas\n");

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ing.txt", ""), archivoMenu, archivoCombos);
        }, "Debe lanzar ProductoRepetidoException cuando hay combos con el mismo nombre");
    }
    @Test
    @DisplayName("Flujo completo del restaurante sin errores")
    void testFlujoCompletoRestaurante() throws Exception {
        File ing = crearArchivoTemporal("ing.txt", "Queso;500\nTocineta;700\n");
        File menu = crearArchivoTemporal("menu.txt", "Hamburguesa;10000\nPapas;5000\nGaseosa;3000\n");
        File combos = crearArchivoTemporal("combos.txt", "ComboBasico;10%;Hamburguesa;Papas\n");

        restaurante.cargarInformacionRestaurante(ing, menu, combos);

        restaurante.iniciarPedido("Sergio", "Calle 123");
        Pedido pedido = restaurante.getPedidoEnCurso();

        pedido.agregarProducto(restaurante.getMenuBase().get(0)); // Hamburguesa
        pedido.agregarProducto(restaurante.getMenuCombos().get(0)); // ComboBasico

        restaurante.cerrarYGuardarPedido();

        File factura = new File("./facturas/factura_" + pedido.getIdPedido() + ".txt");
        assertTrue(factura.exists(), "Debe haberse generado la factura correctamente");
        factura.delete();
    }
    
    @Test
    @DisplayName("Debe cargar combos válidos sin lanzar excepciones")
    void testCargarCombosCorrectamente() throws Exception {
        File menu = crearArchivoTemporal("menu_valido.txt", "Hamburguesa;10000\nPapas;5000\n");
        File combos = crearArchivoTemporal("combos_valido.txt", "ComboFeliz;10%;Hamburguesa;Papas\n");

        restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ing_valido.txt", ""), menu, combos);

        assertEquals(1, restaurante.getMenuCombos().size(), "Debe haberse cargado un combo correctamente");
    }
    
    @Test
    @DisplayName("Debe manejar archivos vacíos sin fallar")
    void testCargarArchivosVacios() throws Exception {
        File archivoIngredientes = crearArchivoTemporal("ingredientes_vacio.txt", "");
        File archivoMenu = crearArchivoTemporal("menu_vacio.txt", "");
        File archivoCombos = crearArchivoTemporal("combos_vacio.txt", "");

        assertDoesNotThrow(() -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        }, "Los archivos vacíos no deben generar excepciones");
    }
    @Test
    @DisplayName("Debe lanzar NumberFormatException si el precio no es numérico")
    void testNumeroFormatoIncorrecto() throws Exception {
        File archivoMenu = crearArchivoTemporal("menu_invalido.txt", "Hamburguesa;precio_invalido\n");
        assertThrows(NumberFormatException.class, () -> {
            restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ing.txt", ""), archivoMenu, crearArchivoTemporal("combos.txt", ""));
        }, "Debe lanzar NumberFormatException si el precio no es numérico");
    }
    @Test
    @DisplayName("Debe crear la carpeta de facturas si no existe")
    void testCreaCarpetaFacturasSiNoExiste() throws Exception {
        File carpeta = new File("./facturas/");
        if (carpeta.exists()) {
            for (File f : carpeta.listFiles()) f.delete();
            carpeta.delete();
        }

        restaurante.iniciarPedido("Sergio", "Calle 123");
        Pedido pedido = restaurante.getPedidoEnCurso();
        pedido.agregarProducto(new ProductoMenu("Hamburguesa", 10000));

        restaurante.cerrarYGuardarPedido();

        assertTrue(carpeta.exists(), "Debe haberse creado la carpeta de facturas automáticamente");

        // limpieza
        for (File f : carpeta.listFiles()) f.delete();
        carpeta.delete();
    }

    @Test
    @DisplayName("Debe leer líneas parcialmente válidas sin lanzar errores")
    void testLineasParcialmenteValidas() throws Exception {
        File archivoIngredientes = crearArchivoTemporal("ingredientes_parcial.txt", "Queso;500\n\nTocineta;700\n");
        assertDoesNotThrow(() -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, crearArchivoTemporal("menu.txt", ""), crearArchivoTemporal("combos.txt", ""));
        }, "Debe ignorar líneas vacías sin lanzar errores");
    }
 
}
