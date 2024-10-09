package com.plog.server.plogging.dto;

import com.plog.server.trash.domain.Trash;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PloggingDetailsResponse {
    private int garbage;
    private int can;
    private int plastic;
    private int paper;
    private int plastic_bag;
    private int glass;

    public PloggingDetailsResponse(Trash trash) {
            this.garbage = trash.getGarbage();
            this.can = trash.getCan();
            this.plastic = trash.getPlastic();
            this.paper = trash.getPaper();
            this.plastic_bag = trash.getPlastic_bag();
            this.glass = trash.getGlass();
        }
}
