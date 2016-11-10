package com.github.wakingrufus.elo.tech.db;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder(toBuilder = true)
@ToString
@Getter
public class Page<T> {
    private final List<T> items;
    private final PageInfo pageInfo;
}
