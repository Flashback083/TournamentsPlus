package fr.pokepixel.tournamentsplus;

import com.google.common.collect.Lists;
import com.hiroku.tournaments.api.Tournament;
import com.hiroku.tournaments.api.events.match.MatchStartEvent;
import com.hiroku.tournaments.api.events.tournament.JoinTournamentEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PixelmonEvent {

    @SubscribeEvent
    public void onTournamentJoin(JoinTournamentEvent event){
        Configuration config = TournamentsPlus.config;
        ConfigCategory cfg = config.getCategory("TournamentsPlusConfig");
        int preset = cfg.get("preset").getInt();
        int maxsamepoke = cfg.get("preventsamepoke").getInt();
        ArrayList<String> spec = Lists.newArrayList(cfg.get("preset"+preset).getStringList());
        event.team.users.forEach(user ->{
            //Player player = event.team.users.get(0).getPlayer().get();
            PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(user.getPlayer().get().getUniqueId());
            for (int i = 0; i < 6; i++){
                Pokemon pokemon = pStorage.get(i);
                int finalI = i;
                spec.forEach(speclist ->{
                    if (new PokemonSpec(speclist).matches(pokemon)){
                        int slot = finalI + 1;
                        user.getPlayer().get().sendMessage(Text.of(TextFormatting.RED + "You can't join the tournament, one pokemon of your team doesnt match with the rules of the tournaments (Slot: " + slot + ")"));
                        event.setCanceled(true);
                    }
                });
            }
            if(maxsamepoke>0 && checkDuplicate(pStorage.getTeam(),maxsamepoke)){
                user.getPlayer().get().sendMessage(Text.of(TextFormatting.RED + "You can't join the tournament, same pokemon limit"));
                event.setCanceled(true);
            }

        });
    }



    @SubscribeEvent
    public void onTournamentStart(MatchStartEvent event){
        Configuration config = TournamentsPlus.config;
        ConfigCategory cfg = config.getCategory("TournamentsPlusConfig");
        int preset = cfg.get("preset").getInt();
        int maxsamepoke = cfg.get("preventsamepoke").getInt();
        ArrayList<String> spec = Lists.newArrayList(cfg.get("preset"+preset).getStringList());
        Tournament.instance().teams.forEach(t -> t.users.forEach(user -> {
           Player p = user.getPlayer().get();
           PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(p.getUniqueId());
           for (int i = 0; i < 6; i++) {
               Pokemon pokemon = pStorage.get(i);
               int finalI = i;
               spec.forEach(speclist -> {
                   if (new PokemonSpec(speclist).matches(pokemon)) {
                       int slot = finalI + 1;
                       p.sendMessage(Text.of(TextFormatting.RED + "You can't participate to the tournament, one pokemon of your team doesnt match with the rules of the tournaments (Slot: " + slot + ")"));
                       Tournament.instance().forfeitTeams(true,t);
                   }
               });
           }
            if(maxsamepoke>0 && checkDuplicate(pStorage.getTeam(),maxsamepoke)){
                p.sendMessage(Text.of(TextFormatting.RED + "You can't join the tournament, same pokemon limit"));
                Tournament.instance().forfeitTeams(true,t);
            }
       }));
    }

    private boolean checkDuplicate(List<Pokemon> team, int maxsamepoke) {
        HashMap<String,Integer> countpoke = new HashMap<>();
        team.forEach(pokemon -> {
            if (countpoke.containsKey(pokemon.getSpecies().getPokemonName())){
                countpoke.put(pokemon.getSpecies().getPokemonName(),countpoke.get(pokemon.getSpecies().getPokemonName()) +1);
            }else {
                countpoke.put(pokemon.getSpecies().getPokemonName(),1);
            }
        });
        AtomicBoolean result = new AtomicBoolean(false);
        countpoke.values().forEach(integer -> {
            if (integer>maxsamepoke){
                result.set(true);
            }
        });
        return result.get();
    }


    

}