"""Comb item: one rounded lump of wax with about three big hex cells across, light walls and dark open cells.
Silhouette letters: '#' comb body, '.' empty. Cells are laid out by beeart.comb()."""
SHAPE = [
    "................",  # 0
    ".....#####......",  # 1
    "....########....",  # 2
    "..############..",  # 3
    "..############..",  # 4
    ".##############.",  # 5
    ".##############.",  # 6
    "################",  # 7
    "################",  # 8
    ".##############.",  # 9
    ".##############.",  # 10
    ".##############.",  # 11
    "..############..",  # 12
    "...##########...",  # 13
    "....#######.....",  # 14
    "................",  # 15
]
# open cell interior (4 x 4 hex), tones on the inner layer: top-left in the lip's shadow, low right lit
CELL = [
    [None, 112, 128, None],
    [120, 150, 166, 182],
    [134, 168, 190, 206],
    [None, 188, 214, None],
]
# cell top-left corners: 2 / 3 / 2 in a hex stagger, one wall pixel between neighbours
CELLS = [(3, 2), (8, 2), (1, 7), (6, 7), (11, 7), (3, 12), (8, 12)]
