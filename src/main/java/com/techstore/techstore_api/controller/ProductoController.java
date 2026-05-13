package com.techstore.techstore_api.controller;

import com.techstore.techstore_api.dto.ProductoDTO;
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        Producto productoActualizado =
                productoService.actualizarProducto(id, dto);

        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {

        productoService.eliminarProducto(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}