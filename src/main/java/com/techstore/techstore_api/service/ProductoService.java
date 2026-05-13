package com.techstore.techstore_api.service;
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.repository.ProductoRepository;
import com.techstore.techstore_api.dto.ProductoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {

        return productoRepository.findAll();
    }

    public Producto crearProducto(
            ProductoDTO dto) {

        Producto producto =
                new Producto();

        producto.setNombre(
                dto.getNombre());

        producto.setDescripcion(
                dto.getDescripcion());

        producto.setPrecio(
                dto.getPrecio());

        producto.setStock(
                dto.getStock());

        producto.setCategoria(
                dto.getCategoria());

        producto.setActivo(true);

        return productoRepository.save(
                producto);
    }

    public Producto actualizarProducto(
            Long id,
            ProductoDTO dto) {

        Optional<Producto> productoOptional =
                productoRepository.findById(id);

        if (productoOptional.isPresent()) {

            Producto producto =
                    productoOptional.get();

            producto.setNombre(
                    dto.getNombre());

            producto.setDescripcion(
                    dto.getDescripcion());

            producto.setPrecio(
                    dto.getPrecio());

            producto.setStock(
                    dto.getStock());

            producto.setCategoria(
                    dto.getCategoria());

            return productoRepository.save(
                    producto);
        }

        return null;
    }

    public void eliminarProducto(Long id) {

        Optional<Producto> productoOptional =
                productoRepository.findById(id);

        if (productoOptional.isPresent()) {

            Producto producto =
                    productoOptional.get();

            producto.setActivo(false);

            productoRepository.save(producto);
        }
    }
}
