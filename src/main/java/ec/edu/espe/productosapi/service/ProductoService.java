package ec.edu.espe.productosapi.service;

import ec.edu.espe.productosapi.exception.ProductoInvalidoException;
import ec.edu.espe.productosapi.exception.ProductoNoEncontradoException;
import ec.edu.espe.productosapi.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final Map<Long, Producto> productos = new LinkedHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    public ProductoService() {
        guardar(new Producto(null, "Laptop", 850.0, 10));
        guardar(new Producto(null, "Mouse", 15.5, 50));
        guardar(new Producto(null, "Teclado", 30.0, 25));
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productos.get(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException(id);
        }
        return producto;
    }

    public Producto crear(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor a cero");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo");
        }
        return guardar(producto);
    }

    public void eliminar(Long id) {
        if (!productos.containsKey(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        productos.remove(id);
    }

    private Producto guardar(Producto producto) {
        producto.setId(contadorId.incrementAndGet());
        productos.put(producto.getId(), producto);
        return producto;
    }
}