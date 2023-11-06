package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderById(long requestorId);

    List<ItemRequest> findAllByRequestorIdIsNotOrderById(long requestorId, Pageable pageable);

}
