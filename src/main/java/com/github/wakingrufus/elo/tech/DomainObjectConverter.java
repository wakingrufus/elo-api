package com.github.wakingrufus.elo.tech;

import java.util.Collection;
import java.util.List;

public interface DomainObjectConverter<D, T> {
    D toDatabase(T dto);
    T toDto(D database);
    List<D> toDatabase(Collection<T> dtos);
    List<T> toDto(Collection<D> databaseObjects);
}
