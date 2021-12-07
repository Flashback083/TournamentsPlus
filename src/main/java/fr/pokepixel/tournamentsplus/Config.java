package fr.pokepixel.tournamentsplus;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

   
    private static final String CATEGORY = "TournamentsPlusConfig";


    private static int preset = 1;
    private static String[] preset1 = new String[]{};
    private static String[] preset2 = new String[]{};
    private static String[] preset3 = new String[]{};
    private static String[] preset4 = new String[]{};
    private static String[] preset5 = new String[]{};
    private static int preventsamepoke = 0;

    public static void readConfig() {
        Configuration cfg = TournamentsPlus.config;
        try {
            cfg.load();   
            initWhitelistConfig(cfg);
        } catch (Exception e1) {
            TournamentsPlus.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }    
    }

    public static void reloadConfig(){
        Configuration cfg = TournamentsPlus.config;
        if (cfg.hasChanged()) {
            cfg.save();
        }
        cfg.load();
    }
  
    private static void initWhitelistConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY, CATEGORY);
        preset = cfg.getInt("preset",CATEGORY,preset,1,5,"Choose which preset it will be use in the plugin");
        preset1 = cfg.getStringList("preset1", CATEGORY, preset1,"List of pokemon (as spec) which will be banned from Tournament plugin");
        preset2 = cfg.getStringList("preset2", CATEGORY, preset2,"List of pokemon (as spec) which will be banned from Tournament plugin");
        preset3 = cfg.getStringList("preset3", CATEGORY, preset3,"List of pokemon (as spec) which will be banned from Tournament plugin");
        preset4 = cfg.getStringList("preset4", CATEGORY, preset4,"List of pokemon (as spec) which will be banned from Tournament plugin");
        preset5 = cfg.getStringList("preset5", CATEGORY, preset5,"List of pokemon (as spec) which will be banned from Tournament plugin");
        preventsamepoke = cfg.getInt("preventsamepoke",CATEGORY,preventsamepoke,0,6,"How many the player can have the same pokemon in their team ? Set 0 to disable this config");
    }
}
