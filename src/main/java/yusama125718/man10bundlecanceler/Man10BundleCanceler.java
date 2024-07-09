package yusama125718.man10bundlecanceler;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.InventoryBlockStartEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Man10BundleCanceler extends JavaPlugin implements Listener {

    static JavaPlugin canceler;
    static Boolean system = false;
    static Boolean world_mode = false;
    static List<String> worlds;
    enum Mode{
        All,
        In,
        Out
    }
    static Mode mode;

    @Override
    public void onEnable() {
        canceler = this;
        canceler.saveDefaultConfig();
        system = canceler.getConfig().getBoolean("system");
        worlds = canceler.getConfig().getStringList("worlds");
        world_mode = canceler.getConfig().getBoolean("world_mode");
        mode = Mode.valueOf(canceler.getConfig().getString("mode"));
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
                sender.sendMessage("[MBC] bundle cancelerをonにしました");
                return true;
            }
            else if(args[0].equals("off")){
                system = false;
                canceler.getConfig().set("system", system);
                canceler.saveConfig();
                sender.sendMessage("[MBC] bundle cancelerをoffにしました");
                return true;
            }
            else if (args[0].equals("world")){
                if (!(sender instanceof Player)){
                    sender.sendMessage("[MBC] コンソールからは実行できません");
                    return true;
                }
                if (worlds.contains(((Player) sender).getWorld().getName())){
                    worlds.remove(((Player) sender).getWorld().getName());
                    canceler.getConfig().set("worlds", worlds);
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] 削除しました");
                } else {
                    worlds.add(((Player) sender).getWorld().getName());
                    canceler.getConfig().set("worlds", worlds);
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] 追加しました");
                }
                return true;
            }
            else if (args[0].equals("state")){
                sender.sendMessage("[MBC] 現在の状態");
                sender.sendMessage("システム:" + system.toString());
                sender.sendMessage("アクション:" + mode.toString());
                sender.sendMessage("ワールドモード:" + world_mode.toString());
                if (world_mode){
                    sender.sendMessage("[MBC] 有効になっているワールド");
                    if (worlds.size() == 0) sender.sendMessage("有効になっているワールドはありません");
                    for (String name : worlds){
                        sender.sendMessage(name);
                    }
                }
                return true;
            }
        }
        if (args.length == 2){
            if (args[0].equals("mode")){
                if (args[1].equals("in")){
                    mode = Mode.In;
                    canceler.getConfig().set("mode", "In");
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] 収納のみ禁止に変更しました");
                }
                else if (args[1].equals("out")){
                    mode = Mode.Out;
                    canceler.getConfig().set("mode", "Out");
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] 取り出しのみ禁止に変更しました");
                }
                else if(args[1].equals("all")){
                    mode = Mode.All;
                    canceler.getConfig().set("mode", "All");
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] 全て禁止に変更しました");
                }
                else sender.sendMessage("[MBC] /mbcanceler mode [in/out/all]でキャンセルするアクションを選択");
                return true;
            }
            else if (args[0].equals("world_mode")){
                if (args[1].equals("on")){
                    world_mode = true;
                    canceler.getConfig().set("world_mode", world_mode);
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] world_modeをonにしました");
                    return true;
                }
                else if(args[1].equals("off")){
                    world_mode = false;
                    canceler.getConfig().set("world_mode", world_mode);
                    canceler.saveConfig();
                    sender.sendMessage("[MBC] world_modeをoffにしました");
                    return true;
                }
                else sender.sendMessage("[MBC] /mbcanceler world_mode [on/off]でワールド単位で禁止か全体かを切り替え");
                return true;
            }
        }
        sender.sendMessage("[MBC] /mbcanceler [on/off]でオンオフを切り替え");
        sender.sendMessage("[MBC] /mbcanceler world_mode [on/off]でワールド単位で禁止か全体かを切り替え");
        sender.sendMessage("[MBC] /mbcanceler worldで今いるワールドでmbcancelerの[on/off]を切り替え");
        sender.sendMessage("[MBC] /mbcanceler mode [in/out/all]でキャンセルするアクションを選択");
        sender.sendMessage("[MBC] /mbcanceler state 現在の設定を表示");
        return true;
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e){
        if (!e.getClick().equals(ClickType.RIGHT) || !system || (world_mode && !worlds.contains(e.getWhoClicked().getWorld().getName()))) return;
        // 収納処理
        if ((mode == Mode.Out || mode == Mode.All) && e.getCursor() != null && e.getCurrentItem() != null && (e.getCursor().getType().equals(Material.BUNDLE) || e.getCurrentItem().getType().equals(Material.BUNDLE))){
            if (e.getCursor().getType().equals(Material.AIR) || e.getCurrentItem().getType().equals(Material.AIR)) e.setCancelled(true);
        }
        // 取り出し処理
        if ((mode == Mode.In || mode == Mode.All) && e.getCursor() != null && e.getCurrentItem() != null && (e.getCursor().getType().equals(Material.BUNDLE) || e.getCurrentItem().getType().equals(Material.BUNDLE))) {
            if (!e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.BUNDLE)) e.setCancelled(true);
            else if (!e.getCurrentItem().getType().equals(Material.AIR) && e.getCursor().getType().equals(Material.BUNDLE)) e.setCancelled(true);
        }
    }
}
