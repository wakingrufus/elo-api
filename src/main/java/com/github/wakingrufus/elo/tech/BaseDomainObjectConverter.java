package com.github.wakingrufus.elo.tech;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseDomainObjectConverter<D, T> implements DomainObjectConverter<D, T> {
    @Override
    public List<D> toDatabase(Collection<T> dtos) {
        List<D> newColl = new ArrayList<>();
        dtos.forEach(d -> newColl.add(toDatabase(d)));
        return newColl;
    }

    @Override
    public List<T> toDto(Collection<D> databaseObjects) {
        List<T> newColl = new ArrayList<>();
        databaseObjects.forEach(d -> newColl.add(toDto(d)));
        return newColl;
    }
}
