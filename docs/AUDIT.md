# Bee audit (2026-11-01, port.2)

Checked with `H:\AI_MC_Modding\_handoff\tools\bee_audit.py` against the installed Simply NeoForged jars, and by a
field-by-field comparison of this branch's generated data with upstream Complicated Bees 3.6.2
(`ComplicatedBees/reference`, revision 68d1299).

| Check | Result |
|---|---|
| Species drops and specialties vs upstream | All 59 species identical (items, comb types, chances). |
| Combs and centrifuge outputs vs upstream | All 23 combs and every centrifuge recipe identical. The only diffs are the 26.1 ingredient format (`bottle_to_droplet`, `propolis` inputs). |
| Mutations vs upstream | Identical parents, results, chances and conditions. |
| Unresolved ids in the pack | 0 |
| Orphan species | None |
| Combs without a centrifuge | 0 |
| Primordial | Upstream gives Primordial one product: 25% Mysterious comb (beeswax, honey, 10% pearl shard) and no specialty. That is the original design, not a port loss; its value is the Ends effect and long-lived, never-sleeping genes. Buffed in port.4, see below. |

No port faults were found.

## Primordial buff (port.4, 2026-11-02)

The author asked for Primordial to be worth breeding ("Buff it"). It is the last bee in the tree (dragon-egg flower, Ends
effect) but only dropped a 25% Mysterious comb. Port.4 changes `species/primordial.json` only:

| | Before (upstream) | After (port.4) |
|---|---|---|
| Product | Mysterious comb 25% | Mysterious comb 40% |
| Specialty | none | Dragon's breath 6%, echo shard 3%, nether star 0.5% |

These are vanilla items, so no condition is needed and no new art. The bee keeps the "slowest" productivity, so the
nether star stays rare.
`complicated_bees-neoforge-3.6.2+26.1.2-port.4.jar` is the installed port.3 archive with only this file and the
`neoforge.mods.toml` version changed. It replaces `complicated_bees-neoforge-3.6.2+26.1.2-port.3.jar` in the pack (the species-mod tooltip build);
Genetic Industry port.2 stays as it is. No 26.2 binary was rebuilt.
The same data change is in the combined repo (ComplicatedBees, branch neoforge-26-ports), which also carries
port.3's species-mod tooltip (BeeItem); this split fork does not have that tooltip change.
