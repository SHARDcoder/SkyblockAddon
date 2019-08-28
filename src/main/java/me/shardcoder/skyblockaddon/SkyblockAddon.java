package me.shardcoder.skyblockaddon;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreInitializationEvent;
import cc.hyperium.internal.addons.IAddon;
import java.util.Timer;
import java.util.TimerTask;
import me.shardcoder.skyblockaddon.commands.SkyblockAddonCommand;
import me.shardcoder.skyblockaddon.listeners.PlayerListener;
import net.minecraft.client.Minecraft;

public class SkyblockAddon implements IAddon {

    @Override
    public void onLoad() {
        EventBus.INSTANCE.register(this);

        System.out.println("[SkyblockAddon] Loaded");
    }

    private static SkyblockAddon instance; // for Mixins cause they don't have a constructor
    private ConfigValues configValues;
    private PlayerListener playerListener = new PlayerListener(this);
    private RenderListener renderListener = new RenderListener(this);
    private Utils utils = new Utils(this);
    private InventoryUtils inventoryUtils = new InventoryUtils(this);
    private Scheduler scheduler = new Scheduler(this);
    private final boolean usingLabymod = false;
    private final boolean usingOofModv1 = false;

    @InvokeEvent
    public void preInit(PreInitializationEvent e) {
        instance = this;
        configValues = new ConfigValues(this, e.getSuggestedConfigurationFile());
    }

    @InvokeEvent
    public void init(InitializationEvent e) {
        EventBus.INSTANCE.register(playerListener);
        EventBus.INSTANCE.register(renderListener);
        EventBus.INSTANCE.register(scheduler);
        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new SkyblockAddonCommand(this));


        //Postinit stuff because we dont have
        configValues.loadConfig();
        utils.checkDisabledFeatures();
        scheduleMagmaCheck();
    }

    private void scheduleMagmaCheck() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft() != null) {
                    utils.fetchEstimateFromServer();
                } else {
                    scheduleMagmaCheck();
                }
            }
        }, 5000);
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public RenderListener getRenderListener() {
        return renderListener;
    }

    public Utils getUtils() {
        return utils;
    }

    public InventoryUtils getInventoryUtils() {
        return inventoryUtils;
    }

    public boolean isUsingLabymod() {
        return usingLabymod;
    }

    public static SkyblockAddon getInstance() {
        return instance;
    }

    public boolean isUsingOofModv1() {
        return usingOofModv1;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void onClose() {


        System.out.println("[SkyblockAddon] Closed");
    }

    @Override
    public void sendDebugInfo() {
    }
}
