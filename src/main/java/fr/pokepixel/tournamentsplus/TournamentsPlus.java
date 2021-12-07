package fr.pokepixel.tournamentsplus;

import com.hiroku.tournaments.Tournaments;
import com.hiroku.tournaments.api.Tournament;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = TournamentsPlus.MOD_ID,
        name = TournamentsPlus.MOD_NAME,
        version = TournamentsPlus.VERSION,
        acceptableRemoteVersions = "*",
        serverSideOnly = true
)
public class TournamentsPlus {

    public static final String MOD_ID = "tournamentsplus";
    public static final String MOD_NAME = "TournamentsPlus";
    public static final String VERSION = "2.1";
    public static Configuration config;
    static Logger logger;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static TournamentsPlus INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        File directory = new File(event.getModConfigurationDirectory(), "TournamentsPlus");
        config = new Configuration(new File(directory.getPath(), "tournamentsplus.cfg"));
        Config.readConfig();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Tournaments.EVENT_BUS.register(new PixelmonEvent());
        Pixelmon.EVENT_BUS.register(new PixelmonEvent());
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    @Mod.EventHandler
    public void onStart(FMLServerStartingEvent event){
        event.registerServerCommand(new TournamentsPlusCmd());
    }

}
