#!/usr/bin/env python3
"""
validate_minerals.py — lightweight validator for chex-minerals.json5

Checks:
- JSON5 can be parsed (comments tolerated by a quick preprocessor)
- minerals.* entries have expected fields
- distribution entries contain plausible ids/tags and numeric ranges
- reports unknown namespaces (heuristic) and malformed ids

Note: This is an offline structural validator. It does not resolve tags/blocks
from a running game registry; that would require a game-side validation pass.
"""
from __future__ import annotations
import re
import sys
from pathlib import Path

CONFIG_REL = Path('forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5')

ID_RE = re.compile(r'^[a-z0-9_\-.]+:[a-z0-9_\-/\.]+$')
KNOWN_NAMESPACES = {
    'minecraft', 'cosmic_horizons_extended', 'gtceu', 'forge'
}

def strip_json5_comments(text: str) -> str:
    out = []
    in_str = False
    esc = False
    i = 0
    while i < len(text):
        c = text[i]
        if in_str:
            out.append(c)
            if esc:
                esc = False
            elif c == '\\':
                esc = True
            elif c == '"':
                in_str = False
            i += 1
            continue
        if c == '"':
            in_str = True
            out.append(c)
            i += 1
            continue
        if c == '/' and i + 1 < len(text):
            n = text[i+1]
            if n == '/':
                # line comment
                i += 2
                while i < len(text) and text[i] not in '\r\n':
                    i += 1
                continue
            if n == '*':
                # block comment
                i += 2
                while i + 1 < len(text) and not (text[i] == '*' and text[i+1] == '/'):
                    i += 1
                i += 2
                continue
        out.append(c)
        i += 1
    return ''.join(out)


def load_json5(path: Path):
    import json
    text = path.read_text(encoding='utf-8')
    stripped = strip_json5_comments(text)
    # Allow trailing commas: remove ",}\n" and ",]" patterns
    stripped = re.sub(r',\s*([}\]])', r'\1', stripped)
    return json.loads(stripped)


def validate():
    root = Path(__file__).resolve().parents[1]
    cfg_path = root / CONFIG_REL
    if not cfg_path.exists():
        print(f'ERROR: config not found: {cfg_path}', file=sys.stderr)
        return 2
    try:
        data = load_json5(cfg_path)
    except Exception as e:
        print(f'ERROR: failed to parse JSON5: {e}', file=sys.stderr)
        return 3

    errors = 0
    warnings = 0

    minerals = data.get('minerals', {})
    if not isinstance(minerals, dict) or not minerals:
        print('ERROR: minerals section missing or empty', file=sys.stderr)
        return 4

    def is_valid_id(s: str) -> bool:
        return bool(ID_RE.match(s))

    def check_ns(s: str):
        nonlocal warnings
        ns = s.split(':', 1)[0] if ':' in s else ''
        if ns and ns not in KNOWN_NAMESPACES:
            print(f'WARN: unknown namespace: {ns} in {s}')
            warnings += 1

    for planet_id, planet in minerals.items():
        if not is_valid_id(planet_id):
            print(f'ERROR: invalid planet id: {planet_id}', file=sys.stderr)
            errors += 1
            continue
        tiers = (planet or {}).get('mineralTiers', [])
        if not isinstance(tiers, list) or not tiers:
            print(f'ERROR: planet {planet_id} missing mineralTiers', file=sys.stderr)
            errors += 1
            continue
        for tier in tiers:
            dists = (tier or {}).get('distributions', [])
            if not isinstance(dists, list) or not dists:
                print(f'WARN: planet {planet_id} tier has no distributions')
                warnings += 1
                continue
            for dist in dists:
                if not isinstance(dist, dict):
                    print(f'ERROR: planet {planet_id} distribution not an object', file=sys.stderr)
                    errors += 1
                    continue
                id_or_tag = dist.get('tag') or dist.get('block') or dist.get('id')
                if not isinstance(id_or_tag, str):
                    print(f'ERROR: planet {planet_id} distribution missing id/tag', file=sys.stderr)
                    errors += 1
                    continue
                # tags may be any string by convention, but ensure plausible form
                if 'tag' in dist:
                    if not id_or_tag or id_or_tag.strip() == '':
                        print(f'ERROR: empty tag in planet {planet_id}', file=sys.stderr)
                        errors += 1
                else:
                    if not is_valid_id(id_or_tag):
                        print(f'ERROR: invalid id {id_or_tag} in planet {planet_id}', file=sys.stderr)
                        errors += 1
                    else:
                        check_ns(id_or_tag)
                for key in ('count', 'minY', 'maxY'):
                    if key in dist and not isinstance(dist[key], int):
                        print(f'ERROR: {key} not int for {id_or_tag} in {planet_id}', file=sys.stderr)
                        errors += 1
                biomes = dist.get('biomes')
                if biomes is not None and not isinstance(biomes, list):
                    print(f'ERROR: biomes not list for {id_or_tag} in {planet_id}', file=sys.stderr)
                    errors += 1

    if errors == 0:
        print('OK: chex-minerals.json5 passed structural validation')
        if warnings:
            print(f'Notes: {warnings} warnings')
        return 0
    print(f'FAIL: {errors} errors, {warnings} warnings', file=sys.stderr)
    return 5


if __name__ == '__main__':
    sys.exit(validate())
