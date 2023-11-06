package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static ItemDtoForGet toItemDtoForGet(Item item) {
        return ItemDtoForGet.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(Collections.emptyList())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequestId() != null ? ItemRequest.builder().id(itemDto.getRequestId()).build() : null)
                .build();
    }

    public static Item toItemForUpdate(ItemDto itemDto, Item itemFromBd) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(itemFromBd.getOwner())
                .name(getName(itemDto, itemFromBd))
                .available(getAvailable(itemDto, itemFromBd))
                .description(getDescription(itemDto, itemFromBd))
                .build();
    }

    private static String getName(ItemDto itemDto, Item itemFromBd) {
        if (itemDto.getName() != null) {
            return itemDto.getName();
        } else if (itemFromBd.getName() != null) {
            return itemFromBd.getName();
        } else {
            return null;
        }
    }

    private static Boolean getAvailable(ItemDto itemDto, Item itemFromBd) {
        if (itemDto.getAvailable() != null) {
            return itemDto.getAvailable();
        } else if (itemFromBd.getAvailable() != null) {
            return itemFromBd.getAvailable();
        } else {
            return null;
        }
    }

    private static String getDescription(ItemDto itemDto, Item itemFromBd) {
        if (itemDto.getDescription() != null) {
            return itemDto.getDescription();
        } else if (itemFromBd.getDescription() != null) {
            return itemFromBd.getDescription();
        } else {
            return null;
        }
    }
}
