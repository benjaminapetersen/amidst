package amidst.gui.menu;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.List;

import amidst.Application;
import amidst.gui.MapWindow;
import amidst.logging.Log;
import amidst.map.MapObjectStronghold;
import amidst.map.layers.StrongholdLayer;
import amidst.minecraft.world.FileWorld.Player;
import amidst.minecraft.world.WorldType;
import amidst.minecraft.world.Worlds;

public class MenuActions {
	private static final String ABOUT_MESSAGE = "Advanced Minecraft Interfacing and Data/Structure Tracking (AMIDST)\n"
			+ "By Skidoodle (amidst.project@gmail.com)";

	private Application application;
	private MapWindow mapWindow;

	public MenuActions(Application application, MapWindow mapWindow) {
		this.application = application;
		this.mapWindow = mapWindow;
	}

	public void savePlayerLocations() {
		if (application.isFileWorld()) {
			application.getWorldAsFileWorld().savePlayerLocations();
		}
	}

	public void exit() {
		application.exitGracefully();
	}

	public void newFromSeed() {
		String seed = mapWindow.askForSeed();
		if (seed != null) {
			if (seed.isEmpty()) {
				newFromRandom();
			} else {
				WorldType worldType = mapWindow.askForWorldType();
				if (worldType != null) {
					application.setWorld(Worlds.fromSeed(seed, worldType));
				}
			}
		}
	}

	public void newFromRandom() {
		WorldType worldType = mapWindow.askForWorldType();
		if (worldType != null) {
			application.setWorld(Worlds.random(worldType));
		}
	}

	public void newFromFileOrFolder() {
		File worldFile = mapWindow.askForMinecraftMapFile();
		if (worldFile != null) {
			try {
				application.setWorld(Worlds.fromFile(worldFile));
			} catch (Exception e) {
				mapWindow.displayException(e);
			}
		}
	}

	public void findStronghold() {
		MapObjectStronghold stronghold = mapWindow
				.askForOptions("Go to", "Select Stronghold:",
						StrongholdLayer.instance.getStrongholds());
		if (stronghold != null) {
			mapWindow.moveMapToCoordinates(stronghold.x, stronghold.y);
		}
	}

	public void gotoCoordinate() {
		long[] coordinates = mapWindow.askForCoordinates();
		if (coordinates != null) {
			mapWindow.moveMapToCoordinates(coordinates[0], coordinates[1]);
		} else {
			mapWindow.displayMessage("You entered an invalid location.");
			Log.w("Invalid location entered, ignoring.");
		}
	}

	public void gotoPlayer() {
		if (application.isFileWorld()) {
			List<Player> playerList = application.getWorldAsFileWorld()
					.getPlayers();
			Player[] playerArray = playerList.toArray(new Player[playerList
					.size()]);
			Player player = mapWindow.askForOptions("Go to", "Select player:",
					playerArray);
			if (player != null) {
				mapWindow.moveMapToCoordinates(player.getX(), player.getZ());
			}
		} else {
			mapWindow.displayMessage("There are no players on the map");
		}
	}

	public void capture() {
		File file = mapWindow.askForScreenshotSaveFile();
		if (file != null) {
			mapWindow.capture(file);
		}
	}

	public void copySeedToClipboard() {
		String seed = "" + application.getWorld().getSeed();
		StringSelection selection = new StringSelection(seed);
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(selection, selection);
	}

	public void checkForUpdates() {
		application.checkForUpdates();
	}

	public void viewLicense() {
		application.displayLicenseWindow();
	}

	public void about() {
		mapWindow.displayMessage(ABOUT_MESSAGE);
	}
}
