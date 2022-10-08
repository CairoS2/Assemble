package io.github.thatkawaiisam.assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import io.github.thatkawaiisam.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

@Getter
public class AssembleBoard
{
	private List<String> identifiers;
	private UUID uuid;
	private Scoreboard scoreboard;
	private Objective objective;
	private Assemble assemble;
	private Objective healthName;
	private List<AssembleBoardEntry> entries;

	private void setup(final Player player) {
		if (this.getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			this.scoreboard = player.getScoreboard();
		}
		else {
			this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		(this.objective = this.scoreboard.registerNewObjective("Default", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName(this.getAssemble().getAdapter().getTitle(player));
		(this.healthName = this.getOrCreateObjective(this.scoreboard, "healthName", "health")).setDisplaySlot(DisplaySlot.BELOW_NAME);
		this.healthName.setDisplayName(ChatColor.DARK_RED + "\u2764");
		player.setScoreboard(this.scoreboard);
		final AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
		Bukkit.getPluginManager().callEvent(createdEvent);
	}

	public List<AssembleBoardEntry> getEntries() {
		return this.entries;
	}

	public Objective getObjective() {
		return this.objective;
	}

	public AssembleBoard(final Player player, final Assemble assemble) {
		this.entries = new ArrayList<AssembleBoardEntry>();
		this.identifiers = new ArrayList<String>();
		this.assemble = assemble;
		this.setup(player);
		this.uuid = player.getUniqueId();
	}

	public List<String> getIdentifiers() {
		return this.identifiers;
	}

	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	private static String getRandomChatColor() {
		return ChatColor.values()[ThreadLocalRandom.current().nextInt(ChatColor.values().length)].toString();
	}

	public String getUniqueIdentifier(final String s) {
		String s2;
		for (s2 = String.valueOf(new StringBuilder(String.valueOf(getRandomChatColor())).append(ChatColor.WHITE)); this.identifiers.contains(s2); s2 = String.valueOf(new StringBuilder(String.valueOf(s2)).append(getRandomChatColor()).append(ChatColor.WHITE))) {}
		if (s2.length() > 16) {
			return this.getUniqueIdentifier(s);
		}
		this.identifiers.add(s2);
		return s2;
	}

	public Objective getOrCreateObjective(final Scoreboard scoreboard, final String displayName, final String s) {
		Objective objective = scoreboard.getObjective(displayName);
		if (objective == null) {
			objective = scoreboard.registerNewObjective(displayName, s);
		}
		objective.setDisplayName(displayName);
		return objective;
	}

	public Assemble getAssemble() {
		return this.assemble;
	}

	public AssembleBoardEntry getEntryAtPosition(final int n) {
		if (n >= this.entries.size()) {
			return null;
		}
		return this.entries.get(n);
	}

}
