package me.despical.autoreloader;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

/**
 * @author Despical
 * <p>
 * Created at 5.03.2024
 */
public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();

		var plugins = getConfig().getStringList("jar-names");

		if (plugins.isEmpty()) return;

		var lastModified = new HashMap<File, Long>();

		for (final var name : plugins) {
			var file = new File("plugins", name + ".jar");

			if (!file.exists()) continue;

			lastModified.put(file, file.lastModified());
		}

        var period = getConfig().getLong("checker-period", 20L);

		getServer().getScheduler().runTaskTimer(this, () -> {
			for (var entry : lastModified.entrySet()) {
				if (entry.getKey().lastModified() > entry.getValue()) {
					getServer().broadcastMessage("Changes detected in " + entry.getKey().getName() + ", reloading the server!");
					getServer().dispatchCommand(getServer().getConsoleSender(), "rl confirm");
					break;
				}
			}
		}, 20, period);
	}
}