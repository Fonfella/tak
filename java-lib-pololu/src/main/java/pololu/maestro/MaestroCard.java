/**
  IAmContent Public Libraries.
  Copyright (C) 2015 Greg Elderfield
  @author Greg Elderfield, support@jarchitect.co.uk
 
  This program is free software; you can redistribute it and/or modify it under the terms of the
  GNU General Public License as published by the Free Software Foundation; either version 2 of 
  the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program;
  if not, write to the Free Software Foundation, Inc., 
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package pololu.maestro;

/**
 * The different types of Pololu Maestro cards.
 * @author Greg Elderfield
 */
public enum MaestroCard {
	
	MAESTRO_MICRO6(6),
	MAESTRO_MINI12(12),
	MAESTRO_MINI18(18),
	MAESTRO_MINI24(24);
	
	private final int channelCount;

	private MaestroCard(int channelCount) {

		this.channelCount = channelCount;
	}

	public int channelCount() {

		return channelCount;
	}
}