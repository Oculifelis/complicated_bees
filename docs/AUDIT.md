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
| Primordial | Upstream gives Primordial one product: 25% Mysterious comb (beeswax, honey, 10% pearl shard) and no specialty. That is the original design, not a port loss; its value is the Ends effect and long-lived, never-sleeping genes. Nothing was changed. |

No fixes were needed in this repo.
