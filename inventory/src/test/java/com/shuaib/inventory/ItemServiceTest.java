package com.shuaib.inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock ItemRepository repo;
    @InjectMocks ItemService service;

    private Item sample() {
        Item i = new Item(); i.setName("Dolo 650"); i.setQuantity(100); i.setPrice(30.5); return i;
    }
    @Test void createSavesItemAndIgnoresClientId() {
        when(repo.save(any(Item.class))).thenAnswer(a -> a.getArgument(0));
        Item in = sample(); in.setId(55L);
        assertNull(service.create(in).getId());
        verify(repo).save(in);
    }
    @Test void getThrowsWhenMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get(99L));
    }
    @Test void updateThrowsWhenMissing() {
        when(repo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, sample()));
    }
    @Test void expiryBeforePurchaseIsRejected() {
        Item i = sample(); i.setPurchaseDate(LocalDate.of(2026, 11, 1)); i.setExpiryDate(LocalDate.of(2026, 10, 1));
        assertThrows(IllegalArgumentException.class, () -> service.create(i));
    }
    @Test void searchUsesNameFilter() {
        when(repo.search("dolo")).thenReturn(List.of(sample()));
        assertEquals(1, service.list(" dolo ").size());
    }
    @Test void deleteRemovesExistingItem() {
        when(repo.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repo).deleteById(1L);
    }
}
