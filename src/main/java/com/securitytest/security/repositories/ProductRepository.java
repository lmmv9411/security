package com.securitytest.security.repositories;

import com.securitytest.security.models.Product;
import com.securitytest.security.models.TypeMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("""
            UPDATE Product p SET
                p.stock = p.stock + :q,
                p.lastMove = CURRENT_TIMESTAMP,
                p.typeMove = :typeMove
                WHERE p.id = :productId
            """)
    void increaseStock(@Param("productId") Long productId, @Param("q") int q, @Param("typeMove") TypeMove typeMove);

    @Modifying
    @Query("""
            UPDATE Product p SET
                p.stock = p.stock - :q,
                p.lastMove = CURRENT_TIMESTAMP,
                p.typeMove = :typeMove
                WHERE p.id = :productId
            """)
    void decreaseStock(@Param("productId") Long productId, @Param("q") int q, @Param("typeMove") TypeMove typeMove);

}
