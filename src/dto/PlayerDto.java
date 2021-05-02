package dto;

public class PlayerDto {
	private String id;
	private String name;
	private String rank;
	private String[][] summon;

	public PlayerDto() {
		summon=new String[7][2];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String[][] getSummon() {
		return summon;
	}

	public void setSummon(String summon, int x, int y) {
		this.summon[x][y] = summon;
	}
	
	
	
	
}
