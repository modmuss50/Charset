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

package pl.asie.simplelogic.gates;

import mcmultipart.api.container.IMultipartContainer;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.multipart.MultipartRedstoneHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Optional;

public class ProxyMultipartPresent extends ProxyMultipart {
	@Override
	public int getWeakPower(IBlockAccess w, BlockPos p, EnumFacing real) {
		Optional<IMultipartContainer> container = MultipartHelper.getContainer(w, p);
		if (container.isPresent()) {
			return MultipartRedstoneHelper.getWeakPower(container.get(), real.getOpposite());
		} else {
			return -1;
		}
	}
}
