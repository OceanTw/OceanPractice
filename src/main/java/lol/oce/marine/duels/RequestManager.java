package lol.oce.marine.duels;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.match.MatchType;
import lol.oce.marine.players.User;
import lol.oce.marine.utils.StringUtils;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class RequestManager implements Listener {

    @Getter
    List<Request> requests;

    public void sendRequest(User sender, User receiver, Kit kit) {
        Request request = new Request(sender, receiver, kit);
        TextComponent accept = new TextComponent(StringUtils.handle("&b&lClick to Accept"));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + sender.getPlayer().getName()));
        receiver.getPlayer().sendMessage(StringUtils.handle("&b&lDuel Request"));
        receiver.getPlayer().sendMessage(StringUtils.handle("&f- Sender: " + sender.getPlayer().getName()));
        receiver.getPlayer().sendMessage(StringUtils.handle("&f- Kit: " + kit.getName()));
        receiver.getPlayer().spigot().sendMessage(accept);

        requests.add(request);

        BukkitTask task = Practice.getInstance().getServer().getScheduler().runTaskLater(Practice.getInstance(), () -> {
            requests.remove(request);
        }, 20 * 60);
    }

    public Request getRequest(User sender, User receiver) {
        return requests.stream().filter(request -> request.getSender().equals(sender) && request.getReceiver().equals(receiver)).findFirst().orElse(null);
    }

    public void acceptRequest(User sender, User receiver) {
        Request request = getRequest(sender, receiver);
        if (request != null) {
            request.getSender().getPlayer().sendMessage(StringUtils.handle("&7&oYour request has been accepted"));
            request.getReceiver().getPlayer().sendMessage(StringUtils.handle("&7&oYou have accepted the request"));
            Arena arena = Practice.getInstance().getArenaManager().getRandomArena(request.getKit());
            if (arena == null) {
                request.getSender().getPlayer().sendMessage(StringUtils.handle("&bNo arenas available"));
                request.getReceiver().getPlayer().sendMessage(StringUtils.handle("&bNo arenas available"));
                requests.remove(request);
                return;
            }
            Practice.getInstance().getMatchManager().startSolo(arena, MatchType.DUEL, request.getKit(), new User[]{request.getSender(), request.getReceiver()}, new User[]{request.getReceiver(), request.getSender()}, false);
            requests.remove(request);
        }
    }
}
