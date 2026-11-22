package com.shuaib.inventory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/items")
public class ItemController {
    private final ItemService service;
    public ItemController(ItemService service) { this.service = service; }

    @GetMapping public List<Item> all(@RequestParam(required = false) String search) { return service.list(search); }
    @GetMapping("/{id}") public Item one(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody Item i) { return service.create(i); }
    @PutMapping("/{id}") public Item update(@PathVariable Long id, @Valid @RequestBody Item i) { return service.update(id, i); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
