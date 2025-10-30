package uniandes.dpoo.hamburguesas.tests;


import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

	//Atributos clase ProductoMenu:
	// String nombre
	// int precioBase
	
	//metodos:
	// ProductoMenu (String nombre, int precioBase)
	// getNombre(): String
	//getPrecio(): int
	//generartextoFactura(): String
	
	private ProductoMenu productoMenu;
	
	@BeforeEach
	public void setUp() {
		productoMenu = new ProductoMenu("Hamburguesa Callejera", 30);
		}
	
	@Test
	@DisplayName("getNombre() devuelve el nombre correcto")
	public void testGetNombre() {
		assertEquals("el nombre es incorrecto", "Hamburguesa Callejera", productoMenu.getNombre());
	}
	@Test
	@DisplayName("getPrecio() devuelve el precio correcto")
	    void testGetPrecio() {
	        assertEquals("el precio es incorrecto", 30, productoMenu.getPrecio());
	    }

	 @Test
	 @DisplayName("generarTextoFactura() genera el texto con el formato esperado")
	 public void testGenerarTextoFactura() {
	      assertEquals("El texto de la factura es incorrecto", "Hamburguesa Callejera\n            30\n",productoMenu.generarTextoFactura());
	    }
	
	}

