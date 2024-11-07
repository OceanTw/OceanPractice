package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OneVersusOneMatch extends Match {

    private final Participant red;
    private final Participant blue;

    public OneVersusOneMatch(User player1, User player2 , Arena arena, List<User> spectators, boolean ranked, Kit kit, MatchType type) {
        super(arena, spectators, ranked, kit, type);

        this.red = new Participant(player1, Color.RED);
        this.blue = new Participant(player2, Color.BLUE);
    }

    @Override
    public List<Participant> getParticipants() {
        return Arrays.asList(red, blue);
    }

    @Override
    public void onStart() {
        //YOUR MATCH START LOGIC IDK WHAT U NEED

        final int[] countdown = {5};

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &5" + countdown[0] + " &fseconds!"));
                }
                if (countdown[0] == 0) {
                    cancel();
                }
                countdown[0]--;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    @Override
    public void end(Participant winner) {

    }

    @Override
    public void onDeath(Participant killer, Participant killed) {

    }

    @Override
    public List<String> getLines(Participant participant) {
        return Collections.emptyList();
    }

    @Override
    public void forfeit(Participant participant) {

    }

    @Override
    public void voidMatch() {

    }

    @Override
    public void teleportAll() {
        red.getPlayer().teleport(getArena().getRedSpawn());
        blue.getPlayer().teleport(getArena().getBlueSpawn());
    }

    @Override
    public void giveKits() {
        red.getPlayer().getInventory().setContents(getKit().getContents());
        blue.getPlayer().getInventory().setContents(getKit().getContents());

        red.getPlayer().getInventory().setContents(getKit().getContents());
        red.getPlayer().getInventory().setArmorContents(getKit().getArmour());
    }

    @Override
    public void checkRules() {
        //THIS IS FOR YOUR KIT RULES LIKE FREEZE PLAYER ON START OR SOMETHING IDK
    }
}
