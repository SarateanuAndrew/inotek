package org.example.exportproback.product.repository;

import org.example.exportproback.product.domain.Product;
import org.example.exportproback.product.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCompanyId(UUID companyId);
    List<Product> findByCategory(ProductCategory category);
}
