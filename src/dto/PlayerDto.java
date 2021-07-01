package dto;

public class PlayerDto {
	private String id;
	private String player_name;
	private String player_rank;
	private String bgimg;
	private String[][] summon=new String[7][2];//img url
	private int[][] summonLevel=new int[7][2];//lvl
	private String[][] summonName=new String[7][2];//name

	public PlayerDto() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return player_name;
	}

	public void setName(String name) {
		this.player_name = name;
	}

	public String getRank() {
		return player_rank;
	}

	public void setRank(String rank) {
		this.player_rank = rank;
	}

	public String[][] getSummon() {
		return summon;
	}

	public void setSummon(String summon, int x, int y) {
		this.summon[x][y] = summon;
	}

	
	public int[][] getSummonLevel() {
		return summonLevel;
	}

	public void setSummonLevel(int summonLevel, int x, int y) {
		this.summonLevel[x][y] = summonLevel;
	}

	public String[][] getSummonName() {
		return summonName;
	}

	public void setSummonName(String summonName, int x, int y) {
		this.summonName[x][y] = summonName;
	}
	
	public String getBgimg() {
		return bgimg;
	}

	public void setBgimg(String bgimg) {
		this.bgimg = bgimg;
	}
	
	
	
}
