package dto;

public class PlayerDto {
	private String id;
	private String name;
	private String rank;
	private String bgimg;
	private String[][] summon=new String[7][2];
	private String[][] summonLevel=new String[7][2];

	public PlayerDto() {
		
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

	
	public String[][] getSummonLevel() {
		return summonLevel;
	}

	public void setSummonLevel(String summonLevel, int x, int y) {
		this.summonLevel[x][y] = summonLevel;
	}

	public String getBgimg() {
		return bgimg;
	}

	public void setBgimg(String bgimg) {
		this.bgimg = bgimg;
	}
	
	
	
}
