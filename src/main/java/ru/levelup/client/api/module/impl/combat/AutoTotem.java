package ru.levelup.client.api.module.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventRotation;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleHendler(name = "AutoTotem", description = "", c = Module.Category.COMBAT)
public class AutoTotem extends Module {


}
