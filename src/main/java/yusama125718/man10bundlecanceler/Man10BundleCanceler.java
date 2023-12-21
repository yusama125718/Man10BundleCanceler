package yusama125718.man10bundlecanceler;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Man10BundleCanceler extends JavaPlugin implements Listener {

    static JavaPlugin canceler;
    static Boolean system = false;
    static List<String> worlds;

    @Override
    public void onEnable() {
        canceler = this;
        canceler.saveDefaultConfig();
        system = canceler.getConfig().getBoolean("system");
        worlds = canceler.getConfig().getStringList("worlds");
        canceler.getServer().getPluginManager().registerEvents(this, canceler);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args){
        if (!sender.hasPermission("mbcanceler.op")) return true;
        if (args.length == 1){
            if (args[0].equals("on")){
                system = true;
                canceler.getConfig().set("system", system);
                canceler.saveConfig();
                sender.sendMessage("bundle cancelerをonにしました");
                return true;
            }
            else if(args[0].equals("off")){
                system = false;
                canceler.getConfig().set("system", system);
                canceler.saveConfig();
                sender.sendMessage("bundle cancelerをoffにしました");
                return true;
            }
            if (args[0].equals("world")){
                if (!(sender instanceof Player)){
                    sender.sendMessage("コンソールからは実行できません");
                    return true;
                }
                if (worlds.contains(((Player) sender).getWorld().getName())){
                    worlds.remove(((Player) sender).getWorld().getName());
                    canceler.getConfig().set("worlds", worlds);
                    canceler.saveConfig();
                    sender.sendMessage("削除しました");
                } else {
                    worlds.add(((Player) sender).getWorld().getName());
                    canceler.getConfig().set("worlds", worlds);
                    canceler.saveConfig();
                    sender.sendMessage("追加しました");
                }
                return true;
            }
        }
        sender.sendMessage("/mbcanceler [on/off]でオンオフを切り替え");
        sender.sendMessage("/mbcanceler worldで今いるワールドでantieggの[on/off]を切り替え");
        return true;
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e){
        if (!system || !worlds.contains(e.getWhoClicked().getWorld().getName())) return;
        if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BUNDLE)) {
            e.setCancelled(true);
        }
        if (e.getCursor() != null && e.getCursor().getType().equals(Material.BUNDLE)) {
            e.setCancelled(true);
        }

    }
}
