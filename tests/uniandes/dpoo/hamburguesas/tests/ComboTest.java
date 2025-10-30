package uniandes.dpoo.hamburguesas.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.*;
import java.util.ArrayList;

public class ComboTest {
	private Combo combo;
	
    
	@BeforeEach
	public void setUp() {
		
		ProductoMenu p1 = new ProductoMenu("Hamburguesa", 30000);
	    ProductoMenu p2 = new ProductoMenu("Papas", 7000);
	    ProductoMenu p3 = new ProductoMenu("Malteada Dracula", 13000);
	    ArrayList<ProductoMenu> itemsCombo = new ArrayList<>();
	    itemsCombo.add(p1);
	    itemsCombo.add(p2);
	    itemsCombo.add(p3);
		combo = new Combo("Combo Halloween", 0.2, itemsCombo);
		}
	
	
	@Test
	@DisplayName("getPrecio() devuelve el precio correcto")
	public void testGetPrecio() {
		int esperado = (int) ((30000 + 7000 + 13000) * 0.2);
		assertEquals("el precio del combo es incorrecto", esperado, combo.getPrecio());
	}
	
	@Test
	@DisplayName("getNombre() devuelve el nombre correcto del combo")
	public void testGetNombre() {
	assertEquals("el nombre del combo es incorrecto", "Combo Halloween", combo.getNombre());
	
	}
	
	@Test
	@DisplayName("generarTextoFactura() devuelve la factura correcta")
	public void testGenerarTextofactura() {
		String texto = combo.generarTextoFactura();
        assertTrue(texto.contains("Combo Halloween"));
        assertTrue(texto.contains(String.valueOf(combo.getPrecio())));
        assertTrue(texto.contains("0.2"));
	}

}


