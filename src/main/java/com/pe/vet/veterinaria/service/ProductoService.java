package com.pe.vet.veterinaria.service;

import com.pe.vet.veterinaria.dao.ProductoDAO;
import com.pe.vet.veterinaria.model.Producto;
import java.util.List;

public class ProductoService {
    private final ProductoDAO productoDAO = new ProductoDAO();

    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }

    public Producto obtenerProducto(int id) {
        if (id <= 0) {
            return null;
        }
        return productoDAO.obtenerPorId(id);
    }

    public boolean registrarProducto(String nombre, String precioTexto, String stockTexto, String categoria, boolean estado) {
        Producto producto = construirProducto(0, nombre, precioTexto, stockTexto, categoria, estado, false);
        return productoDAO.registrar(producto);
    }

    public boolean actualizarProducto(String idTexto, String nombre, String precioTexto, String stockTexto, String categoria, boolean estado) {
        Producto actual = obtenerProducto(parseId(idTexto));
        if (actual == null) {
            throw new IllegalArgumentException("El producto seleccionado no existe.");
        }

        Producto producto = construirProducto(actual.getId(), nombre, precioTexto, stockTexto, categoria, estado, true);
        return productoDAO.actualizar(producto);
    }

    public boolean eliminarProducto(String idTexto) {
        Producto actual = obtenerProducto(parseId(idTexto));
        if (actual == null) {
            throw new IllegalArgumentException("El producto seleccionado no existe.");
        }
        return productoDAO.eliminar(actual.getId());
    }

    private Producto construirProducto(int id, String nombre, String precioTexto, String stockTexto, String categoria, boolean estado, boolean requiereId) {
        String nombreNormalizado = normalizar(nombre);
        if (nombreNormalizado == null || nombreNormalizado.isEmpty() || nombreNormalizado.length() > 100) {
            throw new IllegalArgumentException("Ingrese un nombre valido para el producto.");
        }

        String categoriaNormalizada = normalizar(categoria);
        if (categoriaNormalizada == null || categoriaNormalizada.isEmpty() || categoriaNormalizada.length() > 50) {
            throw new IllegalArgumentException("Ingrese una categoria valida para el producto.");
        }

        double precio = parsePrecio(precioTexto);
        int stock = parseStock(stockTexto);

        Producto producto = new Producto();
        if (requiereId) {
            producto.setId(id);
        }
        producto.setNombre(nombreNormalizado);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoriaNormalizada);
        producto.setEstado(estado);
        return producto;
    }

    private int parseId(String idTexto) {
        String idNormalizado = normalizar(idTexto);
        if (idNormalizado == null || idNormalizado.isEmpty()) {
            throw new IllegalArgumentException("El producto seleccionado no existe.");
        }

        try {
            int id = Integer.parseInt(idNormalizado);
            if (id <= 0) {
                throw new IllegalArgumentException("El producto seleccionado no existe.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El producto seleccionado no existe.");
        }
    }

    private double parsePrecio(String precioTexto) {
        String precioNormalizado = normalizar(precioTexto);
        if (precioNormalizado == null || precioNormalizado.isEmpty()) {
            throw new IllegalArgumentException("Ingrese un precio valido mayor que cero.");
        }

        try {
            double precio = Double.parseDouble(precioNormalizado);
            if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
                throw new IllegalArgumentException("Ingrese un precio valido mayor que cero.");
            }
            return precio;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingrese un precio valido mayor que cero.");
        }
    }

    private int parseStock(String stockTexto) {
        String stockNormalizado = normalizar(stockTexto);
        if (stockNormalizado == null || stockNormalizado.isEmpty()) {
            throw new IllegalArgumentException("Ingrese un stock valido mayor o igual a cero.");
        }

        try {
            int stock = Integer.parseInt(stockNormalizado);
            if (stock < 0) {
                throw new IllegalArgumentException("Ingrese un stock valido mayor o igual a cero.");
            }
            return stock;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingrese un stock valido mayor o igual a cero.");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
