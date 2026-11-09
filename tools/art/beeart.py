"""Complicated Bees fork: bee and comb item textures, drawn for the fork (no upstream pixels).

    python tools/art/beeart.py --write          write every bee/comb texture into src/main/resources
    python tools/art/beeart.py --preview DIR    write review sheets into DIR, nothing in the mod

Bee item model: layer0 <species>_bee_base (RGBA, untinted), layer1 bee_outline (greyscale LA, tinted with the
species colour), layer2 princess/queen crown. Comb item model: layer0 comb_outer (walls, outer colour),
layer1 comb_inner (cells, inner colour); both greyscale LA, the tint multiplies.
"""
import sys
from pathlib import Path

import numpy as np
from PIL import Image

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
TEX = ROOT / "src/main/resources/assets/complicated_bees/textures/item"
sys.path.insert(0, str(HERE))
from bee_design import BASE  # noqa: E402


def hx(s):
    s = s.lstrip("#")
    return tuple(int(s[i:i + 2], 16) for i in (0, 2, 4))


# region of each letter, used by the species recolours
REGION = {"H": "head", "h": "head", "k": "head", "e": "eye", "g": "glint",
          "Y": "body", "y": "body", "u": "body", "S": "stripe", "s": "stripe", "z": "stripe",
          "F": "thorax", "f": "thorax", "x": "thorax", "W": "wing", "w": "wing", "q": "wing", "Q": "wing", "L": "leg", "T": "sting", "a": "leg"}
TONE = {"F": 0, "f": 1, "x": 2, "H": 0, "h": 1, "k": 2, "Y": 0, "y": 1, "u": 2, "S": 0, "s": 1, "z": 2,
        "q": 0, "W": 1, "w": 2, "Q": 3, "e": 0, "g": 0, "L": 0, "T": 0, "a": 0}

# honey bee ramps, light -> shadow; shadows lean red/purple, lights lean warm
HONEY = {
    "head": ["ffd96a", "eeb646", "b8792e"],
    "body": ["ffd650", "e9a73a", "ad6a2a"],
    "thorax": ["a8743a", "7d4c28", "5a3020"],
    "stripe": ["4a2c22", "33191a", "22101c"],
    "wing": ["f8fdff", "d6eaf6", "a6c2dc", "7f93b5"],
    "eye": ["21182c"], "glint": ["e9f3ff"], "leg": ["3a2426"], "sting": ["5e4a4c"],
}


def letters():
    return [list(r) for r in BASE]


def paint(grid, ramps):
    a = np.zeros((16, 16, 4), np.uint8)
    for y, row in enumerate(grid):
        for x, ch in enumerate(row):
            if ch == ".":
                continue
            if isinstance(ch, tuple):  # literal colour from an accessory overlay
                a[y, x] = ch + (255,)
                continue
            reg = REGION[ch]
            ramp = ramps[reg]
            a[y, x, :3] = hx(ramp[min(TONE[ch], len(ramp) - 1)])
            a[y, x, 3] = 255
    return a


def outline_of(alpha, lit=255, mid=214, dark=150):
    """4-neighbour ring around the silhouette, greyscale LA. Lit on the top/left side, dark on the bottom/right."""
    m = alpha > 0
    h, w = m.shape
    out = np.zeros((h, w, 2), np.uint8)
    for y in range(h):
        for x in range(w):
            if m[y, x]:
                continue
            r = x + 1 < w and m[y, x + 1]
            d = y + 1 < h and m[y + 1, x]
            l = x > 0 and m[y, x - 1]
            u = y > 0 and m[y - 1, x]
            if not (r or d or l or u):
                continue
            v = lit if (r or d) and not (l or u) else dark if (l or u) and not (r or d) else mid
            out[y, x] = (v, 255)
    return out


def la(arr):
    return Image.fromarray(arr, "LA")


def rgba(arr):
    return Image.fromarray(arr, "RGBA")


def tint(img, col):
    a = np.array(img.convert("RGBA")).astype(float)
    a[..., :3] *= np.array(hx(col)) / 255.0
    return Image.fromarray(a.astype(np.uint8), "RGBA")


def comp(*layers):
    out = Image.new("RGBA", layers[0].size, (0, 0, 0, 0))
    for l in layers:
        out = Image.alpha_composite(out, l.convert("RGBA"))
    return out


# ---------------------------------------------------------------- species
def R(*cols):
    return list(cols)


# hand-placed tone map for leafy / textured heads (0 lit, 1 mid, 2 shadow), keyed by (x, y) of the head pixels
LEAF = {(2, 6): 0, (3, 6): 1, (4, 6): 0,
        (1, 7): 0, (2, 7): 1, (3, 7): 0, (4, 7): 2, (5, 7): 1,
        (1, 8): 1, (4, 8): 0, (5, 8): 2,
        (1, 9): 0, (4, 9): 2, (5, 9): 1,
        (2, 10): 1, (3, 10): 2, (4, 10): 2}


def head_pixels(grid):
    return [(x, y) for y, row in enumerate(grid) for x, ch in enumerate(row) if isinstance(ch, str) and REGION.get(ch) == "head"]


def leafy(grid, ramp):
    for (x, y) in head_pixels(grid):
        grid[y][x] = hx(ramp[LEAF.get((x, y), 1)])


def put(grid, pts, col):
    for x, y in pts:
        grid[y][x] = hx(col)


def acc_desert(g, s):  # cactus head: ribbed green with pale spines
    leafy(g, s["head"])
    put(g, [(3, 6), (3, 7), (4, 9), (3, 10)], "3d5f2a")      # rib furrows
    put(g, [(2, 6), (1, 8), (5, 7)], "efe7b8")               # spines catching the light


def acc_forest(g, s):  # oak-leaf head
    leafy(g, s["head"])
    put(g, [(2, 7), (5, 9)], "8cc85a")


def acc_jungle(g, s):
    leafy(g, s["head"])
    put(g, [(3, 6), (1, 9)], "a6e26a")
    put(g, [(4, 10)], "1f4f1a")


def acc_swamp(g, s):
    leafy(g, s["head"])
    put(g, [(4, 7)], "2a3a1a")


def acc_taiga(g, s):  # spruce needles: darker, with a snow-free cold highlight
    leafy(g, s["head"])
    put(g, [(2, 6), (1, 9)], "6a8a5a")


def acc_mushroom(g, s):  # red cap (rows 6-8) with white spots over a pale stem face
    cap, stem = s["head"], s["stem"]
    capt = {(2, 6): 0, (3, 6): 0, (4, 6): 1, (1, 7): 0, (2, 7): 1, (3, 7): 0, (4, 7): 1, (5, 7): 2,
            (1, 8): 1, (4, 8): 2, (5, 8): 2}
    for (x, y) in head_pixels(g):
        if (x, y) in capt:
            g[y][x] = hx(cap[capt[(x, y)]])
        else:
            g[y][x] = hx(stem[0 if x <= 2 else 1 if x == 3 else 2])
    put(g, [(3, 6), (4, 7)], "fbf4ea")                       # cap spots
    put(g, [(1, 7)], "f0e2d8")


def acc_plains(g, s):  # dandelion head: petals round an orange heart, the eye peeks out
    pet = s["head"]
    for (x, y) in head_pixels(g):
        g[y][x] = hx(pet[LEAF.get((x, y), 1)])
    put(g, [(4, 8)], "e58a22")
    put(g, [(4, 9)], "b8601c")
    put(g, [(3, 6)], "fff3a0")


def acc_snowy(g, s):  # snow cap on the head top
    put(g, [(2, 6), (3, 6), (4, 6), (1, 7), (2, 7)], "ffffff")
    put(g, [(3, 7), (4, 7)], "e2ecf6")


def acc_jazzy(g, s):  # pale fedora with a black band and a pinched crown; brim wider than the head
    hat = s["hat"]
    for x in (3, 4):
        g[3][x] = hx(hat[0])                                  # pinched top
    for x in (2, 3, 4, 5):
        g[4][x] = hx(hat[0 if x < 4 else 1])
        g[5][x] = hx("15121a")                                # band
    for x in range(1, 7):
        g[6][x] = hx(hat[0 if x < 3 else 1 if x < 6 else 2])
    g[4][2] = hx("fffaf0")                                    # glint, top left
    g[5][5] = hx("d0383c")                                    # small red feather tip in the band


def acc_tricky(g, s):  # white face, red eye
    put(g, [(2, 8), (2, 9), (3, 9)], "d4202a")
    put(g, [(3, 8)], "ff9a9a")


def acc_ender(g, s):
    put(g, [(2, 8), (2, 9), (3, 9)], "9a3ad0")
    put(g, [(3, 8)], "f2c8ff")


SPECIES = {
    "bee": dict(),
    "desert_terraform": dict(head=R("8fbc58", "5f8f38", "3d5f2a"), body=R("f3e3b2", "dac48c", "b29a6a"),
                             stripe=R("b08a58", "8c6a40", "664a32"), thorax=R("cfae78", "a88a58", "7c6242"), acc=acc_desert),
    "ender": dict(head=R("3e3052", "2a1f3a", "181024"), body=R("f4efbe", "dbd39a", "b0a578"),
                  stripe=R("2e2440", "1d1530", "120c1f"), thorax=R("5e4c72", "403256", "2a2040"), acc=acc_ender),
    "forest_terraform": dict(head=R("62a83c", "437f2a", "2b5820"), body=R("c79e60", "a07945", "735330"),
                             stripe=R("4a3320", "33221a", "221418"), thorax=R("8a6a3e", "654a2c", "45311f"), acc=acc_forest),
    "gray": dict(head=R("55555e", "3a3a42", "26262e"), body=R("dadadf", "acacb4", "7c7c88"),
                 stripe=R("45454e", "303038", "1e1e26"), thorax=R("8c8c95", "6a6a73", "4c4c55")),
    "jazzy": dict(head=R("4a3c42", "33282e", "201820"), body=R("a2714f", "7d523e", "573a2e"),
                  stripe=R("2a2226", "1c161a", "100c10"), thorax=R("6e4c3a", "523628", "38241c"),
                  hat=R("f0e6d0", "c8b896", "94846a"), acc=acc_jazzy, own_outline=True),
    "jungle_terraform": dict(head=R("62c43c", "40a026", "2a7020"), body=R("948c40", "726a2e", "504a22"),
                             stripe=R("3a3418", "282414", "1a170e"), thorax=R("6e6430", "524a24", "38321a"), acc=acc_jungle),
    "mushroom_terraform": dict(head=R("e64a3e", "bc2a2a", "861c26"), stem=R("efe4d4", "cdbcac", "a49086"),
                               body=R("a8989a", "84747c", "5e5060"), stripe=R("4e4846", "3a3434", "282428"),
                               thorax=R("c6b6a8", "a29284", "786a62"), acc=acc_mushroom),
    "plains_terraform": dict(head=R("ffe65a", "f5c030", "c08a20"), body=R("80bc4c", "5e963a", "40702c"),
                             stripe=R("2e4a1a", "22381a", "162812"), thorax=R("5e8e34", "466e28", "30501e"), acc=acc_plains),
    "red": dict(head=R("44343a", "2e2228", "1c1418"), body=R("c25440", "9a3630", "682228"),
                stripe=R("2e1a1e", "201214", "140a0e"), thorax=R("7e3430", "5c2424", "3e1618"), own_outline=True),
    "snowy_terraform": dict(head=R("c8d0dc", "a0aabb", "76809a"), body=R("e4f0fb", "bcd2ec", "90a8d0"),
                            stripe=R("7d9ccc", "5e7cb4", "445e94"), thorax=R("c4d4e8", "9eb2d0", "7a8cb0"), acc=acc_snowy),
    "swamp_terraform": dict(head=R("5a7434", "3e5426", "2a3a1c"), body=R("7e7e4c", "60603a", "44442a"),
                            stripe=R("2e3420", "20261a", "161a12"), thorax=R("56583a", "40422c", "2c2e20"), acc=acc_swamp),
    "taiga_terraform": dict(head=R("44603a", "30482c", "203222"), body=R("6e4e32", "523824", "38261a"),
                            stripe=R("201814", "160f0e", "0c0808"), thorax=R("4e3a28", "3a2a1e", "281c14"), acc=acc_taiga),
    "tricky": dict(head=R("ffffff", "e2e2ec", "b4b4c6"), body=R("52b44c", "36923a", "246a2c"),
                   stripe=R("b84cec", "8e2ec8", "60209a"), thorax=R("3e9a3e", "2c7a30", "1e5624"),
                   leg=R("8e2ec8"), acc=acc_tricky),
}


def species_grid(name, stripe=None):
    s = SPECIES[name]
    ramps = dict(HONEY)
    ramps.update({k: v for k, v in s.items() if isinstance(v, list)})
    if stripe:
        ramps["stripe"] = stripe
    g = letters()
    if s.get("acc") is not None or True:
        # resolve letters to colours first so accessories can overwrite single pixels
        pass
    a = paint(g, ramps)
    grid = [[tuple(int(c) for c in a[y, x, :3]) if a[y, x, 3] else "." for x in range(16)] for y in range(16)]
    # accessories work on letter positions, so give them the letter grid with colours kept where painted
    lg = letters()
    for y in range(16):
        for x in range(16):
            if lg[y][x] != "." and REGION[lg[y][x]] != "head":
                lg[y][x] = grid[y][x]
    for (x, y) in head_pixels(lg):
        pass
    if s.get("acc"):
        s["acc"](lg, ramps)
    # remaining head letters get the species head ramp
    out = np.zeros((16, 16, 4), np.uint8)
    for y in range(16):
        for x in range(16):
            ch = lg[y][x]
            if ch == ".":
                continue
            if isinstance(ch, str):
                out[y, x, :3] = hx(ramps[REGION[ch]][min(TONE[ch], len(ramps[REGION[ch]]) - 1)])
            else:
                out[y, x, :3] = ch
            out[y, x, 3] = 255
    return out


# primordial: cream body, stripes that shimmer between black and deep teal over 4 frames, rainbow outline
PRIMORDIAL = dict(head=R("33464c", "1f3036", "101c22"), body=R("f2efc0", "dcd7a2", "b6ae80"),
                  thorax=R("52636a", "3a4a50", "263238"))
PRIM_STRIPES = [R("2a3c42", "16262c", "0a1418"), R("2e4e54", "1c3a40", "0e2328"),
                R("22343a", "122228", "081014"), R("355a5e", "22464a", "123034")]


def primordial_frames():
    SPECIES["primordial"] = dict(PRIMORDIAL)
    return [species_grid("primordial", stripe=st) for st in PRIM_STRIPES]


def rainbow_outline(alpha, frames=4):
    import colorsys
    ring = outline_of(alpha)
    pts = [(x, y) for y in range(16) for x in range(16) if ring[y, x, 1]]
    cx = sum(p[0] for p in pts) / len(pts)
    cy = sum(p[1] for p in pts) / len(pts)
    import math
    out = []
    for f in range(frames):
        a = np.zeros((16, 16, 4), np.uint8)
        for (x, y) in pts:
            ang = (math.atan2(y - cy, x - cx) / (2 * math.pi) + f / frames) % 1.0
            v = ring[y, x, 0] / 255.0
            r, g, b = colorsys.hsv_to_rgb(ang, 0.5, 0.62 + 0.38 * v)
            a[y, x] = (int(r * 255), int(g * 255), int(b * 255), 255)
        out.append(a)
    return np.concatenate(out, axis=0)


# ---------------------------------------------------------------- comb
def comb():
    """returns (outer LA, inner LA) arrays: outer = light wax walls, inner = dark open cells."""
    from comb_design import SHAPE, CELL, CELLS
    m = np.array([[c == "#" for c in r] for r in SHAPE])
    H, W = m.shape
    outer = np.zeros((H, W, 2), np.uint8)
    inner = np.zeros((H, W, 2), np.uint8)

    def inside(x, y):
        return 0 <= x < W and 0 <= y < H and m[y, x]

    def core(x, y):  # the rim stays wall
        return all(inside(x + a, y + b) for a, b in ((0, 0), (1, 0), (-1, 0), (0, 1), (0, -1)))

    cellv = {}
    for x0, y0 in CELLS:
        if True:
            px = [(x0 + dx, y0 + dy, v) for dy, row in enumerate(CELL) for dx, v in enumerate(row)
                  if v is not None and core(x0 + dx, y0 + dy)]
            if len(px) >= 5:
                for x, y, v in px:
                    cellv[(x, y)] = v

    for y in range(H):
        for x in range(W):
            if not m[y, x]:
                continue
            if (x, y) in cellv:
                inner[y, x] = (cellv[(x, y)], 255)
                continue
            up, lf = inside(x, y - 1), inside(x - 1, y)
            dn, rt = inside(x, y + 1), inside(x + 1, y)
            if not up or not lf:
                v = 196 if (not dn or not rt) else 252          # lit rim, top/left
            elif not dn or not rt:
                v = 150                                          # shadow rim, bottom/right
            else:
                lip = (x, y - 1) in cellv or (x - 1, y) in cellv  # wall below/right of a cell faces the light
                over = (x, y + 1) in cellv or (x + 1, y) in cellv
                v = 244 if lip and not over else 206 if over and not lip else 226
                v += ((x * 7 + y * 13) % 5 - 2) * 2              # faint wax grain
            outer[y, x] = (v, 255)
    return outer, inner


def comb_preview(outer, inner):
    """untinted greyscale preview, LA like the original comb.png."""
    out = outer.copy()
    sel = inner[..., 1] > 0
    out[sel] = inner[sel]
    return out


# ---------------------------------------------------------------- write
SHARED_OUTLINE = [n for n in SPECIES if not SPECIES[n].get("own_outline")]
PREVIEW_OUTLINE = "fff4dc"   # bee.png (JEI icon) is the honey bee with a pale outline baked in


def build():
    """returns {file name: PIL image} for every texture this script owns."""
    out = {}
    base = species_grid("bee")
    ring = outline_of(base[..., 3])
    out["bee_base.png"] = rgba(base)
    out["bee_outline.png"] = la(ring)
    out["bee.png"] = comp(rgba(base), tint(la(ring), PREVIEW_OUTLINE))
    for n in SPECIES:
        if n == "bee":
            continue
        a = species_grid(n)
        out[n + "_bee_base.png"] = rgba(a)
        if SPECIES[n].get("own_outline"):
            out[n + "_bee_outline.png"] = la(outline_of(a[..., 3]))
        else:
            assert (a[..., 3] > 0).tolist() == (base[..., 3] > 0).tolist(), n + " leaves the shared outline"
    fr = primordial_frames()
    SPECIES.pop("primordial")
    out["primordial_bee_base.png"] = rgba(np.concatenate(fr, axis=0))
    out["primordial_bee_outline.png"] = rgba(rainbow_outline(fr[0][..., 3]))
    o, i = comb()
    out["comb_outer.png"] = la(o)
    out["comb_inner.png"] = la(i)
    out["comb.png"] = la(comb_preview(o, i))
    return out


def extras(out):
    """Files outside textures/item that showed the same bee: the buzzing particle (greyscale, tinted in code)
    the 128px repo icon (the JEI bee at 8x) and the bee sorter's filter icons."""
    a = np.array(out["bee.png"].convert("RGBA")).astype(float)
    lum = a[..., 0] * 0.3 + a[..., 1] * 0.59 + a[..., 2] * 0.11
    p = np.zeros((16, 16, 4), np.uint8)
    p[..., 0] = p[..., 1] = p[..., 2] = np.clip(lum * 0.55 + 20, 0, 255).astype(np.uint8)
    p[..., 3] = a[..., 3].astype(np.uint8)
    gui_path = "src/main/resources/assets/complicated_bees/textures/gui/bee_sorter.png"
    gui = Image.open(ROOT / gui_path).convert("RGBA")
    marks = Image.open(HERE / "sorter_marks.png").convert("RGBA")  # red ? and X from the sorter's own art
    for i, extra in enumerate([None, "princess_crown.png", "queen_crown.png", None, None]):
        cell = Image.new("RGBA", (16, 16), (139, 139, 139, 255))  # sorter filter icons: drone/princess/queen/?/none
        cell.alpha_composite(out["bee.png"].convert("RGBA"))
        cell.alpha_composite(Image.open(TEX / extra).convert("RGBA") if extra else marks.crop((0, 16 * i, 16, 16 * i + 16)))
        gui.paste(cell, (176, 16 + 16 * i))
    return {gui_path: gui,
            "src/main/resources/assets/complicated_bees/textures/particle/bee.png": Image.fromarray(p, "RGBA"),
            "icon.png": out["bee.png"].convert("RGBA").resize((128, 128), Image.NEAREST)}


if __name__ == "__main__":
    if "--write" in sys.argv:
        for name, img in build().items():
            img.save(TEX / name)
            print("wrote", name, img.mode, img.size)
        for path, img in extras(build()).items():
            img.save(ROOT / path)
            print("wrote", path, img.mode, img.size)
