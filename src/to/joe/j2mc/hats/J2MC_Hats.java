package to.joe.j2mc.hats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;

public class J2MC_Hats extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        J2MC_Manager.getPermissions().addFlagPermissionRelation("j2mc.admintoolkit.hat", 'H', true);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM votes WHERE username LIKE ? AND timestamp > DATE_SUB( NOW(), INTERVAL 24 HOUR)");
            ps.setString(1, event.getPlayer().getName());
            ResultSet rs = ps.executeQuery();
            Player p = event.getPlayer();
            if (rs.next()) {
                p.sendMessage(ChatColor.GREEN + "Thanks for voting in the past 24 hours");
                p.sendMessage(ChatColor.GREEN + "As a reward, you have access to /hat");
                J2MC_Manager.getPermissions().addFlag(p, 'H');
            } else {
                p.sendMessage(ChatColor.RED + "You have not voted in the past 24 hours.");
                ItemStack headgear = p.getInventory().getHelmet();
                if (headgear == null || headgear.getType().equals(Material.GOLD_HELMET) || headgear.getType().equals(Material.IRON_HELMET) || headgear.getType().equals(Material.DIAMOND_HELMET) || headgear.getType().equals(Material.LEATHER_HELMET) || headgear.getType().equals(Material.CHAINMAIL_HELMET) || headgear.getType().equals(Material.PUMPKIN) || headgear.getType().equals(Material.SKULL)) {
                    p.sendMessage(ChatColor.RED + "Visit http://joe.to/vote for details on voting");
                } else {
                    p.sendMessage(ChatColor.RED + "Your hat has been removed");
                    p.getInventory().setHelmet(null);
                }
            }
        } catch (SQLException e) {
            getServer().getLogger().log(Level.SEVERE, "Error reading if player can has hat", e);
        }
    }
}
