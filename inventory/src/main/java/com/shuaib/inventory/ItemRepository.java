package com.shuaib.inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(i.category) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(i.supplier) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Item> search(@Param("q") String q);
}