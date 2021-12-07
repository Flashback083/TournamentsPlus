package fr.pokepixel.tournamentsplus;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.pokepixel.tournamentsplus.Config.reloadConfig;

public class TournamentsPlusCmd extends CommandBase implements ICommand
{
	private final List<String> aliases;

	public TournamentsPlusCmd(){
        aliases = Lists.newArrayList("tournamentsplus");
    }


	@Override
	@Nonnull
	public String getName() {
		return "tournamentsplus";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return 	TextFormatting.LIGHT_PURPLE
				+"/tournamentsplus <reload>\n"
				+TextFormatting.LIGHT_PURPLE + "/tournamentsplus setpreset <preset>";
	}


	@Override
	@Nonnull
	public List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
		if (args.length < 1) {
			sender.sendMessage((new TextComponentString("Command error !")));
			sender.sendMessage((new TextComponentString(getUsage(sender))));
			return;
		}
		Configuration cfg = TournamentsPlus.config;
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			Config.readConfig();
			if (cfg.hasChanged()) {
				cfg.save();
			}
			cfg.load();
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "[TournamentsPlus] Config reloaded!"));
		}else if (args.length == 2 && args[0].equalsIgnoreCase("setpreset")){
			int preset = Integer.parseInt(args[1]);
			Config.readConfig();
			cfg.getCategory("TournamentsPlusConfig").get("preset").set(preset);
			reloadConfig();
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "[TournamentsPlus] Preset changed!"));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	@Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		//priceshop <reload|set> <type> <name> <price>
  	  if (args.length == 1)
      {
  		return getListOfStringsMatchingLastWord(args, "reload");
      }
      else
      {
    	  return Collections.emptyList();
      }
    }

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}
	




	
}