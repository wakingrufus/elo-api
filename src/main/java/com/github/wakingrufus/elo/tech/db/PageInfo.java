package com.github.wakingrufus.elo.tech.db;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@ToString
public class PageInfo {
    private final int page;
    private final int pageSize;
    private final int totalItems;
}
