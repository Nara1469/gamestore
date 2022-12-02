package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.TShirt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TShirtRepository extends JpaRepository<TShirt, Integer> {
    List<TShirt> findAllTShirtsByColor(String color);
    List<TShirt> findAllTShirtsBySize(String size);
}
