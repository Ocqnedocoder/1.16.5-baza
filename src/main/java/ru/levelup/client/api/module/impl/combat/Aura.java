package ru.levelup.client.api.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventPacketReciew;
import ru.levelup.client.api.event.impl.EventRotation;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.module.setting.impl.ModeSetting;
import ru.levelup.client.api.utils.aura.AuraComp;
import ru.levelup.client.api.utils.aura.AuraUtils;
import ru.levelup.client.api.utils.misc.TimerUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.MathHelper.wrapDegrees;

@ModuleHendler(name = "AuraTest", c = Module.Category.COMBAT)
public class Aura extends Module{

    // нету пусто

}
