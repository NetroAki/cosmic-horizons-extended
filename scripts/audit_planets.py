"""
Planet Definition & Discovery Log Audit Tool

This script audits the planet definitions and discovery logs in the CHEX mod.
It checks for consistency, missing data, and potential issues.
"""

import os
import json
import re
from pathlib import Path
from typing import Dict, List, Set, Tuple, Optional, Any

# Paths
MOD_ROOT = Path(r"C:\Users\zacpa\intellij\Cosmic_Horizons_Expanded")
FORGE_SRC = MOD_ROOT / "forge/src/main"
JAVA_SRC = FORGE_SRC / "java/com/netroaki/chex"
RESOURCES = FORGE_SRC / "resources"
DATA = RESOURCES / "data"
ASSETS = RESOURCES / "assets"
LANG_FILE = ASSETS / "cosmic_horizons_extended/lang/en_us.json"

# Load language file for name lookups
try:
    with open(LANG_FILE, 'r', encoding='utf-8') as f:
        LANG = json.load(f)
except FileNotFoundError:
    LANG = {}
    print(f"Warning: Could not find language file at {LANG_FILE}")

class PlanetAuditor:
    def __init__(self):
        self.planets: List[Dict[str, Any]] = []
        self.issues: List[str] = []
        self.duplicate_ids: Dict[str, List[Dict]] = {}
        self.missing_localizations: Set[str] = set()

    def extract_planet_definitions(self) -> None:
        """Extract planet definitions from Java source files."""
        planet_file = JAVA_SRC / "registry/PlanetRegistry.java"
        
        if not planet_file.exists():
            self.issues.append(f"Error: Could not find PlanetRegistry.java at {planet_file}")
            return
            
        with open(planet_file, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Find all new PlanetDef() calls
        pattern = r'new\s+PlanetDef\s*\(([^;]+?)\)'
        matches = re.finditer(pattern, content, re.DOTALL)
        
        for match in matches:
            try:
                args_str = match.group(1)
                # Clean up the arguments
                args_str = re.sub(r'\s+', ' ', args_str)  # Normalize whitespace
                args = [a.strip() for a in re.split(r',(?![^\[\]]*\])', args_str)]
                
                # Parse ID (first argument)
                id_match = re.search(r'ResourceLocation\.fromNamespaceAndPath\(\s*"([^"]+)"\s*,\s*"([^"]+)"\s*\)', args[0])
                if not id_match:
                    self.issues.append(f"Could not parse planet ID from: {args[0]}")
                    continue
                    
                namespace, path = id_match.groups()
                planet_id = f"{namespace}:{path}"
                
                # Parse name (second argument)
                name = args[1].strip('"')
                
                self.planets.append({
                    'id': planet_id,
                    'namespace': namespace,
                    'path': path,
                    'name': name,
                    'raw_args': args,
                    'source_file': str(planet_file.relative_to(MOD_ROOT)),
                    'line_number': content[:match.start()].count('\n') + 1
                })
                
            except Exception as e:
                self.issues.append(f"Error parsing planet definition: {e}\nArgs: {args_str}")

    def check_duplicate_ids(self) -> None:
        """Check for duplicate planet IDs."""
        id_map = {}
        for planet in self.planets:
            if planet['id'] in id_map:
                if planet['id'] not in self.duplicate_ids:
                    self.duplicate_ids[planet['id']] = [id_map[planet['id']]]
                self.duplicate_ids[planet['id']].append(planet)
            else:
                id_map[planet['id']] = planet

    def check_localizations(self) -> None:
        """Check if all planets have proper localization entries."""
        for planet in self.planets:
            name_key = f"planet.{planet['namespace']}.{planet['path']}"
            if name_key not in LANG:
                self.missing_localizations.add(f"{planet['id']}: Missing name localization '{name_key}'")

    def validate_planet(self, planet: Dict) -> List[str]:
        """Validate a single planet definition."""
        issues = []
        
        # Check ID format
        if ':' not in planet['id']:
            issues.append("Invalid ID format, expected 'namespace:path'")
        
        # Check name length
        if len(planet['name']) > 64:
            issues.append(f"Name too long ({len(planet['name'])} > 64 chars)")
            
        # Check for required fields in raw args
        required_args = 12  # Number of parameters in PlanetDef constructor
        if len(planet['raw_args']) < required_args:
            issues.append(f"Missing required arguments (found {len(planet['raw_args'])} < {required_args})")
        
        return issues

    def generate_report(self) -> str:
        """Generate a comprehensive audit report."""
        report = []
        report.append("=" * 80)
        report.append("PLANET DEFINITION AUDIT REPORT")
        report.append("=" * 80)
        report.append(f"Total planets found: {len(self.planets)}\n")
        
        # Report duplicate IDs
        if self.duplicate_ids:
            report.append("❌ DUPLICATE PLANET IDs FOUND:")
            for planet_id, planets in self.duplicate_ids.items():
                report.append(f"\n  {planet_id} ({len(planets)} occurrences):")
                for p in planets:
                    report.append(f"    - {p['source_file']}:{p['line_number']}")
            report.append("")
        else:
            report.append("✅ No duplicate planet IDs found\n")
        
        # Report validation issues
        validation_issues = []
        for planet in self.planets:
            issues = self.validate_planet(planet)
            if issues:
                validation_issues.append((planet['id'], issues, planet['source_file'], planet['line_number']))
        
        if validation_issues:
            report.append("❌ VALIDATION ISSUES FOUND:")
            for planet_id, issues, source_file, line_num in validation_issues:
                report.append(f"\n  {planet_id} ({source_file}:{line_num}):")
                for issue in issues:
                    report.append(f"    - {issue}")
            report.append("")
        else:
            report.append("✅ All planet definitions are valid\n")
        
        # Report missing localizations
        if self.missing_localizations:
            report.append("❌ MISSING LOCALIZATIONS:")
            for loc in sorted(self.missing_localizations):
                report.append(f"  - {loc}")
            report.append("")
        else:
            report.append("✅ All planets have proper localizations\n")
        
        # Report discovery log status
        report.append("DISCOVERY LOG STATUS:")
        # TODO: Add discovery log validation once implemented
        report.append("  ⚠️ Discovery log validation not yet implemented")
        
        return "\n".join(report)

    def run_audit(self) -> None:
        """Run the complete audit."""
        print("Starting planet definition audit...")
        
        # Run all checks
        self.extract_planet_definitions()
        self.check_duplicate_ids()
        self.check_localizations()
        
        # Generate report
        report = self.generate_report()
        print("\n" + report)
        
        # Save report to file
        report_path = MOD_ROOT / "reports" / "planet_audit_report.txt"
        os.makedirs(report_path.parent, exist_ok=True)
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write(report)
        
        print(f"\nAudit complete! Report saved to: {report_path}")

if __name__ == "__main__":
    auditor = PlanetAuditor()
    auditor.run_audit()
