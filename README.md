# Complicated Bees: unofficial NeoForge 26.x port

This branch (`port/26.x`) ports ACCBDD's [Complicated Bees](https://github.com/ACCBDD/complicated_bees) 3.6.2 to Minecraft Java **26.1.2** and **26.2** on NeoForge. It is not an official upstream release. The original mod's README is kept as [README-upstream.md](README-upstream.md).

The branch starts at upstream `1.21.1` revision `68d1299ef688ff9a844c5e16b37876c5122712b5`, so GitHub's compare view shows exactly what the port changed.

| Minecraft | NeoForge tested | Java | Optional JEI tested | Optional Jade tested |
| --- | --- | --- | --- | --- |
| 26.1.2 | 26.1.2.122 | 25 | 29.47.0.111 | 26.1.12+neoforge |
| 26.2 | 26.2.0.93 | 25 | 30.42.0.237 | 26.2.10+neoforge |

The two targets are independent builds: use the jar matching your Minecraft version. JEI and Jade are optional and are not bundled.

The Genetic Industry addon port lives in its own fork: [eriritarded/Complicated-Bees-Genetic-Industry](https://github.com/eriritarded/Complicated-Bees-Genetic-Industry) (branch `port/26.x`). It needs this mod's jar for the same Minecraft version.

## What changed

* Sources under `src/main` are adapted to native 26.x APIs (official names): native component codecs, Value I/O persistence, transactional capabilities, native recipe serializers, ItemModel baking for bee models and GUI extraction.
* `src/targets/<minecraft>/` holds per-target Java and resource overlays. A resource file there replaces the shared file at the same path for that target only.
* EMI integration is kept as source but excluded from compilation, because no EMI release exists for these targets.
* The original data generators (`src/datagen`) and the old bee model loader (`src/legacy-client`) are kept as inactive reference source. The generated resources under `src/generated` ship in the jar.
* Biomes O' Plenty biome modifiers that named removed BoP biomes (`aspen_glade`, `jacaranda_glade`, and `grassland` in `plains_bee_nest.json`) are removed or trimmed; see PORT_STATUS.md.

## Build

Install Java 25, then run from PowerShell:

```powershell
.\build-port.ps1                # both targets
.\build-port.ps1 -Target 26.2   # one target
```

Or call Gradle directly, one target per invocation, keeping a separate project cache per target:

```sh
./gradlew build -PmcTarget=26.1.2 --project-cache-dir .gradle/26.1.2
```

Gradle 9.2.1 and ModDevGradle 2.0.148 are pinned. The jar appears in `build/<minecraft>/libs/complicated_bees-neoforge-3.6.2+<minecraft>-port.2.jar`. Add `-PwithJei` / `-PwithJade` to put those mods on the `runClient` / `runServer` classpath.

## Validation

The port was checked with a dedicated-server fixture (130 assertions per target) and a connected-client run (7 assertions plus screenshots per target) covering both this mod and Genetic Industry together. That harness depends on both mods, so it lives in the Genetic Industry fork (`src/validation`). The reports and screenshots are copied here under `evidence/26.1.2` and `evidence/26.2`. See [PORT_STATUS.md](PORT_STATUS.md) for what was and was not verified; complete gameplay parity is not claimed.

`tools/` holds the one-time migration scripts used to produce the port; see `tools/README.md`.

## Credits and licence

Complicated Bees is by ACCBDD and contributors (EMI plugin by @Abbie5), MIT licensed; see [LICENSE.txt](LICENSE.txt), which also notes that the bee and comb textures are not covered by it. The `LGPLv3.txt` file is kept from upstream. Original mod IDs, textures, recipes, species definitions and translations are retained. The port's changes are offered under the same MIT terms.
