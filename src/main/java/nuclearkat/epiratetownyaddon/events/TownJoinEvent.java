package nuclearkat.epiratetownyaddon.events;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.tasks.CooldownTimerTask;
import nuclearkat.epiratetownyaddon.EpirateTownyAddon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class TownJoinEvent implements Listener {

    private final EpirateTownyAddon epirateTownyAddon;

    public TownJoinEvent(EpirateTownyAddon epirateTownyAddon) {
        this.epirateTownyAddon = epirateTownyAddon;
    }
    @EventHandler
    public void onTownJoin(TownAddResidentEvent event) {

        Player player = event.getResident().getPlayer();

        if (epirateTownyAddon.disallowTownJoinOption) {
            if (player == null) {
                Bukkit.getLogger().log(Level.WARNING, "Could not process invite as either inviter / invited player is null!");
                return;
            }

            if (!CooldownTimerTask.hasCooldown(player.getName(), "TownHop Cooldown")) {

                epirateTownyAddon.setCooldown(player);
                player.sendMessage(epirateTownyAddon.startCooldownMessage);
            } else {
                String remainingCooldownHours = epirateTownyAddon.getRemainingCooldownHours(player);
                String remainingTimeMsg = ChatColor.translateAlternateColorCodes('&', epirateTownyAddon.remainingTimeMessage.replace("%hours%", String.valueOf(remainingCooldownHours)));

                event.getResident().removeTown();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', epirateTownyAddon.onCooldownMessage + " \n " + remainingTimeMsg));
            }
        }
    }
}