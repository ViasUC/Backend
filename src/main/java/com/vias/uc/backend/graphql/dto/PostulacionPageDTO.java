package com.vias.uc.backend.graphql.dto;

import com.vias.uc.backend.model.Postulacion;
import java.util.List;

public class PostulacionPageDTO {
    private final List<Postulacion> items;
    private final int total;
    private final int page;
    private final int size;

    public PostulacionPageDTO(List<Postulacion> items, int total, int page, int size) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<Postulacion> getItems() { return items; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
