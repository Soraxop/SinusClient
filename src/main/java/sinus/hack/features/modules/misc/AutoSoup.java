package sinus.hack.features.modules.misc;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import sinus.hack.features.modules.Module;
import sinus.hack.setting.Setting;
import sinus.hack.utility.player.InventoryUtility;
import sinus.hack.utility.player.SearchInvResult;

public class AutoSoup extends Module {
    public AutoSoup() {
        super("AutoSoup", Category.MISC);
    }

    private final Setting<Float> health = new Setting<>("TriggerHealth", 7f, 1f, 20f);

    @Override
    public void onUpdate() {
        if (mc.player.getHealth() <= health.getValue()) {
            SearchInvResult result = InventoryUtility.findItemInHotBar(Items.MUSHROOM_STEW);
            int prevSlot = mc.player.getInventory().selectedSlot;
            if (result.found()) {
                result.switchTo();
                sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
                InventoryUtility.switchTo(prevSlot);
            }
        }
    }
}
