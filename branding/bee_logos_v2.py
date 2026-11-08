"""Round-2 logos for the bee mods (Complicated Bees, Career Bees, Apiflux, Genetic Industry) in the pixel style of
bannerkit (H:/AI_MC_Modding/_toolkit/logokit): the mods own 16x bee sprite on a 24x24 badge, coloured outlines.

    python bee_logos_v2.py      # writes cb_, career_, apiflux_, gi_ 24/48/520 px PNGs into the current folder
"""
import math, sys
sys.path.insert(0, 'H:/AI_MC_Modding/_toolkit/logokit')
from bannerkit import *
from PIL import Image
CB = 'H:/AI_MC_Modding/ComplicatedBees-fork/src/main/resources/assets/complicated_bees/textures/'
CAR = 'H:/AI_MC_Modding/CareerBees/common/src/main/resources/assets/careerbees/textures/items/bees/'

def mask_of(im, ox=0, oy=0):
    return {(ox + x, oy + y) for y in range(im.height) for x in range(im.width) if im.getpixel((x, y))[3] > 128}

def comb_bg(cv, cols, wall, s=7):
    """Pixel honeycomb: Voronoi cells of a triangular lattice (hexagons), 1-pixel walls, each cell lit from its top left."""
    pts = [(i * s + (s / 2 if j % 2 else 0) - 1, j * s * 0.866 - 1) for j in range(-1, 8) for i in range(-1, 8)]
    for y in range(cv.h):
        for x in range(cv.w):
            d = sorted((math.hypot(x - px, y - py), px, py) for px, py in pts)
            if d[1][0] - d[0][0] < 1.0:
                cv.put(x, y, wall[1 if y > cv.h * 0.55 else 0])
            else:
                _, px, py = d[0]
                t = 0.75 - 0.5 * y / cv.h - 0.12 * ((x - px) + (y - py)) / s
                cv.put(x, y, ramp(cols, t, x, y))

def frame(cv, ink, r=3):
    round_corners(cv.im, r)

def bee(over=None):
    b = sprite(CB + 'item/bee_base.png')
    if over:
        o = sprite(over)
        b = b.copy(); b.alpha_composite(o)
    return b

def badge(subject_imgs, bgfn, ink, n=24, floor=None):
    cv = Canvas(n, n)
    bgfn(cv)
    m = set()
    for im, ox, oy in subject_imgs:
        if ox is None:  # centre its visible part
            l, t, r, b = im.getbbox()
            ox, oy = (n - (r - l)) // 2 - l, (n - (b - t)) // 2 - t
        for x, y in mask_of(im, ox, oy):
            cv.put(x, y, im.getpixel((x - ox, y - oy)))
        m |= mask_of(im, ox, oy)
    jappa_outline(cv, m, floor=floor or ink)
    # rim
    for y in range(n):
        for x in range(n):
            if x in (0, n - 1) or y in (0, n - 1):
                cv.put(x, y, ink)
    round_corners(cv.im, 3)
    return cv.im

def save2(im, out):
    im.save(out + '_%d.png' % im.width)
    im.resize((48, 48), Image.NEAREST).save(out + '_48.png')
    up(im, 520 // im.width).save(out + '_520.png')

honey = palette(CB + 'item/honey_droplet.png', 5)
def cb_logo():
    wax = [HEX('#6e3e12'), HEX('#8a5216'), HEX('#a2621a')]
    return badge([(bee(), None, None)], lambda cv: comb_bg(cv, wax, (HEX('#d99a2a'), HEX('#b8781e'))), HEX('#2a1406'), floor=HEX('#3a1e0a'))

stone = [HEX('#2b2b33'), HEX('#3d3c45'), HEX('#55535c'), HEX('#6e6b72')]
def career_logo():
    def bg(cv):
        comb_bg(cv, stone[:3], (stone[3], stone[2]))
        for (x, y, c) in ((3, 18, '#e8b04a'), (19, 4, '#7fd6e8'), (20, 19, '#e8b04a'), (4, 5, '#c0605a')):
            cv.put(x, y, HEX(c))
    return badge([(bee(CAR + 'mining.png'), None, None)], bg, HEX('#18181e'), floor=HEX('#46404a'))

brass = [HEX('#2c302b'), HEX('#4e534c'), HEX('#6f756c'), HEX('#959b90'), HEX('#c3c8bb')]
def apiflux_logo():
    def bg(cv):
        sky(cv, [HEX('#1d2430'), HEX('#28323f'), HEX('#33404f')], 0.9, 0.0)
        cx, cy = 11.5, 11.5
        cog = set()
        for y in range(cv.h):
            for x in range(cv.w):
                a = math.atan2(y - cy, x - cx); d = math.hypot(x - cx, y - cy)
                tooth = (a / (2 * math.pi) * 8 + 8.25) % 1 < 0.5
                if d <= 8.2 or (tooth and d <= 10.6):
                    cog.add((x, y))
                    rim = d > 7.2 or (tooth and d > 8.2)
                    lit = (x - cx) + (y - cy) < 0
                    cv.put(x, y, brass[(4 if lit else 1) if rim else 2 if d > 4.5 else 3 if d > 2.5 else 0])
        jappa_outline(cv, cog, floor=brass[0])
    return badge([(bee(), None, None)], bg, HEX('#141a22'), floor=HEX('#3a2410'))

GENES = [HEX('#e0503c'), HEX('#f2c040'), HEX('#4fa8e8'), HEX('#6cc850')]
def gi_logo(n=24):
    cv = Canvas(n, n)
    sky(cv, [HEX('#101a1e'), HEX('#16252a'), HEX('#1d3236'), HEX('#244046')], 0.75, 0.0)
    A = [HEX('#1f5a66'), HEX('#2f8a96'), HEX('#4cc0c4'), HEX('#a6ecdf')]
    B = [HEX('#5a2250'), HEX('#8e3a7a'), HEX('#c862a8'), HEX('#f0a6d2')]
    cx, amp, k, ph = 11.5, 6.2, 2 * math.pi / 15, 0.6
    back, front = {}, {}
    for y in range(1, n - 1):
        for s_, pal in ((0, A), (math.pi, B)):
            a = y * k + ph + s_
            x = cx + amp * math.sin(a)
            depth = math.cos(a)
            tgt = front if depth > 0 else back
            for dx in (-1, 0, 1):
                px = round(x + dx * 0.9)
                lv = 1 + (1 if depth > 0.4 else 0) + (1 if dx < 0 else 0) - (1 if dx > 0 else 0)
                if depth <= 0:
                    lv = max(0, lv - 1)
                tgt[(px, y)] = pal[max(0, min(3, lv))]
    rungs = {}
    for y in range(2, n - 2, 2):
        a = y * k + ph
        x0, x1 = cx + amp * math.sin(a), cx + amp * math.sin(a + math.pi)
        if abs(x0 - x1) < 3:
            continue
        g = GENES[(y // 2) % 4]
        lo, hi = sorted((round(x0), round(x1)))
        for x in range(lo + 2, hi - 1):
            rungs[(x, y)] = g if x < (lo + hi) / 2 else GENES[((y // 2) + 2) % 4]
    for d in (back, rungs, front):
        for (x, y), c in d.items():
            cv.put(x, y, c)
    jappa_outline(cv, set(back) | set(front) | set(rungs), floor=HEX('#0e1416'))
    for y in range(n):
        for x in range(n):
            if x in (0, n - 1) or y in (0, n - 1):
                cv.put(x, y, HEX('#0a1012'))
    round_corners(cv.im, 3)
    return cv.im

if __name__ == '__main__':
    for name, fn in (('cb', cb_logo), ('career', career_logo), ('apiflux', apiflux_logo), ('gi', gi_logo)):
        save2(fn(), name)
