package me.thiagocodex.getspawners;

import me.thiagocodex.getspawners.nms_1_13_R1.ItemStack_1_13_R1;
import me.thiagocodex.getspawners.customconfig.CustomConfig;
import me.thiagocodex.getspawners.customconfig.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveSpawners extends Messages {
    public void makeSpawner(CommandSender commandSender, Player receiver, String spawnerLore, String spawnerAmount, String spawnerType) {

        String spawnerName = color(CustomConfig.getSpawners().getString("Spawner_Name." + firstCapitalWord(spawnerLore)));

        spawnerLore = color(CustomConfig.getSpawners().getString("Spawner_Lore." + firstCapitalWord(spawnerLore)).replaceAll("%owner%", receiver.getName()));

        String gave = GAVE;

        String received = RECEIVED;

        Material material = Material.SPAWNER;
        ItemStack itemStack = new ItemStack(material, Integer.parseInt(spawnerAmount));
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        String[] lores = spawnerLore.split(", ");
        Collections.addAll(lore, lores);
        itemMeta.setDisplayName(ChatColor.WHITE + spawnerName);
        itemMeta.setLore(lore);

        switch (Messages.bukkitVersion) {
            case "1.13" -> {
                itemStack.setItemMeta(itemMeta);
                itemStack = ItemStack_1_13_R1.itemStack(itemStack, spawnerType);
                itemStack.setItemMeta(itemStack.getItemMeta());
            }
            case "1.13.1", "1.13.2" -> {
                itemMeta.getCustomTagContainer().setCustomTag(new NamespacedKey(CustomConfig.getSpawners, "SpawnerType"), ItemTagType.STRING, firstCapitalWord(spawnerType));
                itemStack.setItemMeta(itemMeta);
            }
            case "1.14", "1.14.1", "1.14.2", "1.14.3", "1.14.4", "1.15", "1.15.1", "1.15.2", "1.16", "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5", "1.17" -> {
                itemMeta.getPersistentDataContainer().set(new NamespacedKey(CustomConfig.getSpawners, "SpawnerType"), PersistentDataType.STRING, firstCapitalWord(spawnerType));
                itemStack.setItemMeta(itemMeta);
            }
        }
        receiver.getInventory().addItem(itemStack);
        gave = gave.replaceAll("%amount%", spawnerAmount)
                .replaceAll("%spawner_name%", spawnerName)
                .replaceAll("%player%", receiver.getName());
        received = received.replaceAll("%amount%", spawnerAmount)
                .replaceAll("%spawner_name%", spawnerName)
                .replaceAll("%sender%", commandSender.getName());
        commandSender.sendMessage(PREFIX + " " + gave);
        receiver.sendMessage(PREFIX + " " + received);
    }
}