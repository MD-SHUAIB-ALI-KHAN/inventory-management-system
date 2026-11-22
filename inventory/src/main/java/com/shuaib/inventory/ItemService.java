package com.shuaib.inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ItemService {
    private final ItemRepository repo;
    public ItemService(ItemRepository repo) { this.repo = repo; }

    public List<Item> list(String search) {
        return (search == null || search.isBlank()) ? repo.findAll() : repo.search(search.trim());
    }
    public Item get(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)); }

    @Transactional public Item create(Item item) {
        checkDates(item); item.setId(null); return repo.save(item);
    }
    @Transactional public Item update(Long id, Item item) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException(id);
        checkDates(item); item.setId(id); return repo.save(item);
    }
    @Transactional public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException(id);
        repo.deleteById(id);
    }
    // business rule: expiry date must not be before purchase date
    private void checkDates(Item i) {
        if (i.getPurchaseDate() != null && i.getExpiryDate() != null && i.getExpiryDate().isBefore(i.getPurchaseDate()))
            throw new IllegalArgumentException("Expiry date cannot be before purchase date");
    }
}
