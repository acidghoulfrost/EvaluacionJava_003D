package com.techstore.techstore_api.controller;

import com.techstore.techstore_api.dto.ProductoDTO;
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(
                productoService.listarProductos()
        );
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @RequestBody ProductoDTO dto) {

        Producto productoCreado =
                productoService.crearProducto(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoDTO dto) {

        Optional<Producto> productoActualizado =
                productoService.actualizarProducto(id, dto);

        if (productoActualizado.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                productoActualizado.get()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {

        boolean eliminado =
                productoService.eliminarProducto(id);

        if (!eliminado) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}
