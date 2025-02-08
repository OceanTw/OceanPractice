package lol.oce.marine.match.impl;

import lol.oce.marine.match.Match;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class MatchSnapshot {
    private final ItemStack[] inv;
    private final ItemStack[] armour;
    private double health;
    private int pots;
    private int hits;
    private int gaps;

    public MatchSnapshot(Player player) {
        this.inv = player.getInventory().getContents();
        this.armour = player.getInventory().getArmorContents();
        this.health = 0;
        this.pots = 0;
        this.hits = 0;
        this.gaps = 0;
    }

    public void addPots() {
        pots++;
    }

    public void handleHits(Match match) {
        hits++;
    }

    public void addGaps() {
        gaps++;
    }

    public void handleRegen() {
        health += 1;
    }
}
