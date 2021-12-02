package com.gbf.gbf_ff_1030.dto;

import lombok.Data;

@Data
public class PlayerDto {
    private String id;
    private String name;
    private String rank;
//    private String bgimg;
    private String[][] summon=new String[7][2];//img url
    private int[][] summonLevel=new int[7][2];//lvl
    private String[][] summonName=new String[7][2];//name
    private String favorite;

    public void setSummon(String summon, int x, int y) {
        this.summon[x][y] = summon;
    }

    public void setSummonLevel(int summonLevel, int x, int y) {
        this.summonLevel[x][y] = summonLevel;
    }

    public void setSummonName(String summonName, int x, int y) {
        this.summonName[x][y] = summonName;
    }
}
