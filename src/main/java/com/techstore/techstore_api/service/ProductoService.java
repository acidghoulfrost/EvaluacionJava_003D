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

    @Autowired
    private AuditPublisherService auditPublisherService;

    public List<Producto> listarProductos() {

        return productoRepository.findByActivoTrue();
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

        Producto productoGuardado =
                productoRepository.save(
                producto);

        auditPublisherService.publicarEvento(
                "CREAR",
                productoGuardado);

        return productoGuardado;
    }

    public Optional<Producto> actualizarProducto(
            Long id,
            ProductoDTO dto) {

        Optional<Producto> productoOptional =
                productoRepository.findById(id);

        if (productoOptional.isPresent()
                && Boolean.TRUE.equals(productoOptional.get().getActivo())) {

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

            Producto productoGuardado =
                    productoRepository.save(
                    producto);

            auditPublisherService.publicarEvento(
                    "MODIFICAR",
                    productoGuardado);

            return Optional.of(productoGuardado);
        }

        return Optional.empty();
    }

    public boolean eliminarProducto(Long id) {

        Optional<Producto> productoOptional =
                productoRepository.findById(id);

        if (productoOptional.isPresent()
                && Boolean.TRUE.equals(productoOptional.get().getActivo())) {

            Producto producto =
                    productoOptional.get();

            producto.setActivo(false);

            Producto productoGuardado =
                    productoRepository.save(producto);

            auditPublisherService.publicarEvento(
                    "ELIMINAR",
                    productoGuardado);

            return true;
        }

        return false;
    }
}
