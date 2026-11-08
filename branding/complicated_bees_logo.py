"""The Complicated Bees (fork) logo, 40x40 (saved at 40 and 520 px), painted in the house style of the Fundamentum and Ostentum logos
with the shared kit H:/AI_MC_Modding/_toolkit/logokit/logokit.py. A neutral logo for our fork; it does not copy the original mod's logo. Not wired into mods.toml yet: that waits for
Oculifelis's verdict on the review page.

    python branding/complicated_bees_logo.py            # writes branding/complicated_bees_logo_<option>_40.png and _520.png
"""
import math
import os
import sys

sys.path.insert(0, 'H:/AI_MC_Modding/_toolkit/logokit')
from logokit import *  # noqa: E402,F403

def complicated_bees():
    img = backdrop(glow=(240, 180, 50), dark=(30, 18, 10))
    hexgrid(img, (120, 70, 20), 4.0, 0.45)
    L = layer()
    bee(L, 19, 22, 1.45)
    finish(img, L)
    return img


OPTIONS = {'bee': complicated_bees}

if __name__ == '__main__':
    here = os.path.dirname(os.path.abspath(__file__))
    for name, fn in OPTIONS.items():
        save(fn(), os.path.join(here, 'complicated_bees_logo_' + name))
