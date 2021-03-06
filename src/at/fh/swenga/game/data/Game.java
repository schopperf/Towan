package at.fh.swenga.game.data;

import static at.fh.swenga.game.helpers.Artist.*;
import static at.fh.swenga.game.helpers.Scorer.SaveScore;

import org.lwjgl.input.Mouse;

import at.fh.swenga.game.UI.UI;
import at.fh.swenga.game.UI.UI.Menu;
import at.fh.swenga.game.dependencies.slick.Texture;
import at.fh.swenga.game.helpers.StateManager;
import at.fh.swenga.game.helpers.StateManager.GameState;

public class Game {

	private Grid grid;
	private Player player;
	private WaveManager waveManager;
	private UI gameUI;
	private Menu towerPickerMenu;
	private Texture menuBackground;
	private Enemy[] enemyTypes; 
	
	public Game(Grid grid) {
		this.grid = grid;
		this.menuBackground = QuickLoad("backgroundTowerPicker");
		setupUI();
		setupEnemies();
	}
	
	private void setupEnemies(){
		this.enemyTypes = new Enemy[3];
		this.enemyTypes[0] = new EnemyTroll(0, 0, grid);
		this.enemyTypes[1] = new EnemyDragon(0, 0, grid);
		this.enemyTypes[2] = new EnemyOgre(0, 0, grid);
		this.waveManager = new WaveManager(enemyTypes, 1, 10);
		this.player = new Player(grid, waveManager);
		player.setup();
	}
	
	private void setupUI() {
		gameUI = new UI();
		gameUI.createMenu("TowerPicker", WIDTH, TILE_SIZE * 2, MENU_WIDTH, HEIGHT, 2, 0);
		towerPickerMenu = gameUI.getMenu("TowerPicker");
		towerPickerMenu.quickAdd("TowerMonkey", "towerMonkeyFull");
		towerPickerMenu.quickAdd("TowerSling", "towerSlingFull");
		towerPickerMenu.quickAdd("TowerIce", "towerIceFull");
		towerPickerMenu.quickAdd("TowerLightning", "towerLightningFull");
	}
	
	private void updateUI() {
		gameUI.draw();
		gameUI.drawString(WIDTH + TILE_SIZE / 4, 200, "Lives: " + Player.Lives);
		gameUI.drawString(WIDTH + TILE_SIZE / 4, 220, "Gold: " + Player.Gold);
		gameUI.drawString(WIDTH + TILE_SIZE / 4, 300, "Score:");
		gameUI.drawString(WIDTH + TILE_SIZE / 4, 320, "" + Player.Score);
		gameUI.drawString(WIDTH + TILE_SIZE / 4, 400, "Wave: " + waveManager.getWaveNumber());
		gameUI.drawString(0, 0, StateManager.framesInLastSecond + " fps");
		
		if (Mouse.next()){
			boolean mouseClicked = Mouse.isButtonDown(0);
			if (mouseClicked) {
				if (towerPickerMenu.isButtonClicked("TowerMonkey"))
					player.pickTower(new TowerMonkey(TowerType.TowerMonkey,grid.getTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
				if (towerPickerMenu.isButtonClicked("TowerSling"))
					player.pickTower(new TowerSling(TowerType.TowerSling,grid.getTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
				if (towerPickerMenu.isButtonClicked("TowerIce"))
					player.pickTower(new TowerIce(TowerType.TowerIce,grid.getTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
				if (towerPickerMenu.isButtonClicked("TowerLightning"))
					player.pickTower(new TowerLightning(TowerType.TowerLightning,grid.getTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
			}
		}
	}
	
	public void update() {
		if (Player.Lives == 0 && Player.GameOver == true){
			//UPDATE DB
			int new_enemies_slain = Player.Enemies_slain;
			int new_waves_completed = Player.Waves_completed;
			int new_towers_built = Player.Towers_built;
			int new_playtime = 1;
			UpdateDB(new_enemies_slain, new_waves_completed, new_towers_built, new_playtime);
			Player.GameOver = false;
		}
		if (Player.Lives > 0) {
			DrawQuadTex(menuBackground, WIDTH, 0, MENU_WIDTH, HEIGHT);
			grid.draw();
			waveManager.update();
			player.update();
			updateUI();
		} else {
			if (Player.Score > Player.HighScore)
				SaveScore(HIGHSCORE_FILENAME, Player.Score);
			StateManager.setState(GameState.MAINMENU);
		}
	}	
	
	public void UpdateDB(int new_enemies_slain, int new_waves_completed, int new_towers_built, int new_playtime){
		// GET DATA FROM DB
		String sql="SELECT total_enemies_slain,total_waves_completed,total_towers_built,playtime FROM user WHERE user.id='user_id'";
		
		
		
		// UPDATE DATA
		
		
		// WRITE NEW DATA TO DB
		
	}
}
