/*
 * Copyright (c) 2015, 2016, 2017, 2018 Adrian Siekierka
 *
 * This file is part of Charset.
 *
 * Charset is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Charset is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Charset.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.asie.charset.lib.capability.impl;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.asie.charset.api.carry.CustomCarryHandler;
import pl.asie.charset.api.carry.ICarryHandler;

public class CustomCarryHandlerCactus extends CustomCarryHandler {
    public CustomCarryHandlerCactus(ICarryHandler handler) {
        super(handler);
    }

    @Override
    public void tick() {
        Entity carrier = owner.getCarrier();
        if (carrier.getEntityWorld().getTotalWorldTime() % 40 == 0) {
            carrier.attackEntityFrom(DamageSource.CACTUS, 1.0F);
        }
    }
}
