package me.jumper251.replay.vagt.gui;

import dk.plexhost.core.builders.ItemBuilder;
import dk.plexhost.core.builders.SkullBuilder;
import dk.plexhost.core.gui.guis.GuiItem;
import dk.plexhost.core.gui.guis.PaginatedGui;
import dk.plexhost.core.utils.ColorUtils;
import dk.plexit.vagt.utils.LocationUtils;
import me.jumper251.replay.vagt.VagtReplay;
import me.jumper251.replay.vagt.VagtReplayManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

public class VagtReplayMenu {

    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void show(Player player, boolean showOld){
        PaginatedGui gui = new PaginatedGui(5, ColorUtils.getColored((showOld ? "&9&lGamle " : "&9&lSeneste ") + "Vagt Replays"));
        gui.disableAllInteractions();
        gui.getFiller().fillBorder(new GuiItem(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short)3).build()));

        gui.setItem(Arrays.asList(15, 24, 25, 33), new GuiItem(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short)3).build()));

        gui.setItem(16, new GuiItem(new ItemBuilder(Material.CHEST, 1)
                .setDisplayName("&a&lVis seneste replays")
                .setLore("&8&l » &7Klik for at se nye replays.")
                .glow(!showOld)
                .build(), (event) -> show(player, false)));

        gui.setItem(34, new GuiItem(new ItemBuilder(Material.ENDER_CHEST, 1)
                .setDisplayName("&c&lVis gamle replays")
                .setLore("&8&l » &7Klik for at se gamle replays.")
                .glow(showOld)
                .build(), (event) -> show(player, true)));

        Collection<VagtReplay> replays = showOld ? VagtReplayManager.getOldReplays() : VagtReplayManager.getNewReplays();

        if(replays.size() > 15) {

            // Next Page
            gui.setItem(38, new GuiItem(new ItemBuilder(SkullBuilder.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ3M2NmNjZkMzFiODNjZDhiODY0NGMxNTk1OGMxYjczYzhkOTczMjNiODAxMTcwYzFkODg2NGJiNmE4NDZkIn19fQ=="))
                    .setDisplayName("&f&lForrige side")
                    .setLore("&8&l » &7Klik for at gå til den forrige side.")
                    .build(), event -> gui.previous()));

            // Previous Page
            gui.setItem(40, new GuiItem(new ItemBuilder(SkullBuilder.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ=="))
                    .setDisplayName("&f&lNæste side")
                    .setLore("&8&l » &7Klik for at gå til den næste side.")
                    .build(), event -> gui.next()));
        }

        for(VagtReplay replay : replays){
            gui.addItem(new GuiItem(new ItemBuilder(SkullBuilder.itemFromUuid(replay.getUniqueId()))
                    .setDisplayName(replay.getVagtRank().getColor()+"&l" + replay.getLastName())
                    .setLore(
                            "&7Rank&8: &f"+replay.getVagtRank().getPrefix(),
                            "&7Tidspunkt&8: &f"+format.format(replay.getTimestamp()),
                            "&7Location&8: &f"+ LocationUtils.toString(replay.getLocation()),
                            "",
                            "&8&l » &7Klik for at se replays."
                    )
                    .build(), (event) -> {
                player.closeInventory();
                VagtReplayManager.showReplay(player, replay.getReplayID());
            }));
        }

        gui.open(player);
    }
}
