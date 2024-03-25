package dev.lugami.bridge.bukkit.parameters.param.filter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.bridge.global.filter.FilterAction;
import dev.lugami.qlib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilterActionParameter implements ParameterType<FilterAction> {

    @Override
    public FilterAction transform(CommandSender sender, String source) {
        FilterAction filterAction = null;
        try {
            filterAction = FilterAction.valueOf(source);
        }catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "There is no such filter action by the name of \"" + source + "\".");
        }
        return filterAction;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (FilterAction filterAction : FilterAction.values()) {
            if (StringUtils.startsWithIgnoreCase(filterAction.name(), source)) {
                completions.add(filterAction.name());
            }
        }
        return completions;
    }
}
