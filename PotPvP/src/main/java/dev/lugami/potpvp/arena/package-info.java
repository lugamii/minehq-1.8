/**
 * Handles reading and accessing {@link dev.lugami.potpvp.arena.Arena}s
 * and {@link dev.lugami.potpvp.arena.ArenaSchematic}s.
 *
 * An ArenaSchematic is a template for creating Arenas. ArenaSchematics correlate
 * with a .schematic file which can be pasted, and Arenas correlate with a specific
 * paste of said schematic file. Common attributes such as min/max player count
 * are stored in ArenaSchematics, as they are shared between all instances of an Arena,
 * where as specific things such as spawn points (which change per instance) are stored
 * in an Arena.
 */
package dev.lugami.potpvp.arena;