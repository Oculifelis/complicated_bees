"""Base bee design (16x16 item sprite), letter grid. Faces left, head at x1-6 y6-9 (Career Bees overlays rely on it),
two upright paddle wings over the thorax, a round drooping abdomen whose tip is at x14. Light from the top left.

Letters are material + tone (upper = lit, lower = mid, the third letter = shadow):
  thorax fuzz F f x
  head  H h k      eye  e (dark)  g (glint)
  body  Y y u      stripes  S s z      fuzz tip on thorax  F
  wing  W w        wing rim  q (lit side)  Q (shadow side)
  legs  L          stinger  T          antenna  a
"""
BASE = [
    "................",  # 0
    "........qQ......",  # 1
    ".......qWWQ.....",  # 2
    ".......qWwQ.qQ..",  # 3
    "...a...qWwQqWwQ.",  # 4
    "....a...qwQqwQ..",  # 5
    "..HHH..FFQ.Q....",  # 6
    ".HHHhhFFfYSYYs..",  # 7
    ".HeghkFffYSYysy.",  # 8
    ".HeehkfffySyysuT",  # 9
    "..hhkxfxuysyuzu.",  # 10
    "....L.xxuuzuuz..",  # 11
    "...L...L.uzuu...",  # 12
    "........L.......",  # 13
    "................",  # 14
    "................",  # 15
]
