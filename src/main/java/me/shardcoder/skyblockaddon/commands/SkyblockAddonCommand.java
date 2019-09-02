package me.shardcoder.skyblockaddon.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.ChatColor;
import java.util.Collections;
import java.util.List;
import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.listeners.PlayerListener;

public class SkyblockAddonCommand implements BaseCommand {

    private SkyblockAddon main;

    public SkyblockAddonCommand(SkyblockAddon main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "skyblockaddon";
    }

    @Override
    public String getUsage() {
        return "/skyblockaddon";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("edit")) {
                main.getUtils().setFadingIn(false);
                main.getRenderListener().setGuiToOpen(PlayerListener.GUIType.EDIT_LOCATIONS);
                return;

            } else if (args[0].equalsIgnoreCase("nbt")) {

                boolean copyingNBT = !main.getUtils().isCopyNBT();
                main.getUtils().setCopyNBT(copyingNBT);

                if (copyingNBT) {
                    main.getUtils().sendMessage(ChatColor.GREEN + "You are now able to copy the nbt of items. Hover over any item and press CTRL to copy.");
                } else {
                    main.getUtils().sendMessage(ChatColor.RED + "You have disabled the ability to copy nbt.");
                }

                return;
            }
        }
        main.getUtils().setFadingIn(true);
        main.getRenderListener().setGuiToOpen(PlayerListener.GUIType.MAIN);
    }


    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.singletonList("skyblockaddon");
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("sba");
    }
}
