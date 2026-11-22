package com.shuaib.inventory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
@Data @Entity @Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank(message = "Name is required") private String name;
    private String category;
    private String batchNo;
    @Min(value = 0, message = "Quantity cannot be negative") private int quantity;
    @DecimalMin(value = "0.0", message = "Price cannot be negative") private double price;
    private String supplier;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
}
