package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String> {
    Fee findByProductType(String product);
}
